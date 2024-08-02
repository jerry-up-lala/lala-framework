package com.jerry.up.lala.framework.boot.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.jerry.up.lala.framework.boot.response.ResponseUtil;
import com.jerry.up.lala.framework.common.exception.Errors;
import com.jerry.up.lala.framework.common.exception.ServiceException;
import com.jerry.up.lala.framework.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * <p>Description: excel 工具
 *
 * @author FMJ
 * @date 2023/11/14 16:11
 */
@Slf4j
public class ExcelUtil {

    public static <T> List<T> read(MultipartFile file, Class<T> clazz) {
        try {
            ExcelReader excelReader = cn.hutool.poi.excel.ExcelUtil.getReader(file.getInputStream());
            Map<String, String> headerAliasMap = headerAliasMap(clazz, true);
            if (headerAliasMap != null) {
                excelReader.setHeaderAlias(headerAliasMap);
            }
            return excelReader.readAll(clazz);
        } catch (Exception e) {
            throw ServiceException.error(Errors.UPLOAD_ERROR, e);
        }
    }

    public static <T> List<ExcelCheckErrorBO> check(T uploadBO) {
        return check(uploadBO, null);
    }

    public static <T> List<ExcelCheckErrorBO> check(T uploadBO, BiFunction<String, Object, ExcelCheckErrorBO> checkFunction) {
        List<ExcelCheckErrorBO> result = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(uploadBO.getClass());
        if (ArrayUtil.isNotEmpty(fields)) {
            Arrays.stream(fields).forEach(field -> {
                ExcelCheckErrorBO excelCheckErrorBO = loadExcelCheckBO(field, uploadBO, checkFunction);
                if (excelCheckErrorBO != null) {
                    result.add(excelCheckErrorBO);
                }
            });
        }
        return result;
    }

    public static <T> ExcelCheckErrorBO loadExcelCheckBO(Field field, T uploadBO) {
        return loadExcelCheckBO(field, uploadBO, null);
    }

    public static <T> ExcelCheckErrorBO loadExcelCheckBO(Field field, T uploadBO, BiFunction<String, Object, ExcelCheckErrorBO> checkFunction) {
        ExcelFormat excelFormat = field.getAnnotation(ExcelFormat.class);
        if (excelFormat != null) {
            String headerName = StringUtil.isNotNull(excelFormat.headerAlias()) ? excelFormat.headerAlias() : field.getName();
            Object value = ReflectUtil.getFieldValue(uploadBO, field);
            if (excelFormat.require()) {
                if (value == null || (String.class.equals(field.getType()) && StringUtil.isNull((String) value))) {
                    return new ExcelCheckErrorBO().setHeaderName(headerName).setErrorMessage("不能为空");
                }
            }
            if (value != null) {
                if (String.class.equals(field.getType())) {
                    if (StringUtil.isNotNull((String) value)) {
                        int minLength = excelFormat.minLength();
                        if (minLength != 0 && ((String) value).length() < minLength) {
                            return new ExcelCheckErrorBO().setHeaderName(headerName)
                                    .setValue((String) value)
                                    .setErrorMessage("最小长度为" + minLength + ",当前长度为" + ((String) value).length());
                        }
                        int maxLength = excelFormat.maxLength();
                        if (maxLength != 0 && ((String) value).length() > maxLength) {
                            return new ExcelCheckErrorBO().setHeaderName(headerName)
                                    .setValue((String) value)
                                    .setErrorMessage("最大长度为" + maxLength + ",当前长度为" + ((String) value).length());
                        }
                    }
                }
                if (BigDecimal.class.equals(field.getType())) {
                    Double zero = 0d;

                    double minValue = excelFormat.minValue();
                    if (zero.compareTo(minValue) != 0) {
                        if (((BigDecimal) value).doubleValue() < minValue) {
                            return new ExcelCheckErrorBO().setHeaderName(headerName)
                                    .setValue(value.toString())
                                    .setErrorMessage("最小值为" + NumberUtil.decimalFormat("#.##", minValue));
                        }
                    }

                    double maxValue = excelFormat.maxValue();
                    if (zero.compareTo(maxValue) != 0) {
                        if ((((BigDecimal) value).doubleValue()) > maxValue) {
                            return new ExcelCheckErrorBO().setHeaderName(headerName)
                                    .setValue(value.toString())
                                    .setErrorMessage("最大值为" + NumberUtil.decimalFormat("#.##", maxValue));
                        }
                    }
                }

                if (checkFunction != null) {
                    // 处理特殊校验逻辑
                    ExcelCheckErrorBO excelCheckBO = checkFunction.apply(field.getName(), value);
                    if (excelCheckBO != null) {
                        return excelCheckBO.setHeaderName(headerName);
                    }
                }
            }
        }
        return null;
    }

    public static String checkMessage(Integer row, ExcelCheckErrorBO excelCheckErrorBO) {
        return "第" + StringUtil.fontRed(row.toString()) + "行," + excelCheckErrorBO.getHeaderName() +
                StringUtil.fontRed(excelCheckErrorBO.getValue(), ":") + "," + excelCheckErrorBO.getErrorMessage() + "<br>";
    }

    public static <T> ResponseEntity<InputStreamResource> export(String fileName, List<T> data, Class<T> clazz) {
        ExcelWriter excelWriter = cn.hutool.poi.excel.ExcelUtil.getWriter(true);
        Map<String, String> headerAliasMap = headerAliasMap(clazz, false);
        if (headerAliasMap != null) {
            excelWriter.setHeaderAlias(headerAliasMap);
            for (int i = 0; i < headerAliasMap.keySet().size(); i++) {
                excelWriter.setColumnWidth(i, 25);
            }
        }
        StyleSet style = excelWriter.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        excelWriter.write(data);

        excelWriter.setRowHeight(0, 20);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excelWriter.flush(out, true);
        excelWriter.close();
        return ResponseUtil.download(IoUtil.toStream(out), fileName);
    }

    public static ResponseEntity<InputStreamResource> template(String filePath, String fileName) {
        try {
            InputStream is = new ClassPathResource(filePath).getInputStream();
            return ResponseUtil.download(is, fileName);
        } catch (Exception e) {
            throw ServiceException.error(Errors.DOWNLOAD_ERROR, e);
        }
    }

    public static <T> Map<String, String> headerAliasMap(Class<T> clazz, boolean upload) {
        Field[] fields = ReflectUtil.getFields(clazz, item -> {
            ExcelFormat excelFormat = item.getAnnotation(ExcelFormat.class);
            return excelFormat != null && StringUtil.isNotNull(excelFormat.headerAlias());
        });
        if (ArrayUtil.isNotEmpty(fields)) {
            return upload ? Arrays.stream(fields).collect(Collectors.toMap(field -> field.getAnnotation(ExcelFormat.class).headerAlias()
                    , Field::getName, (key1, key2) -> key1)) :
                    Arrays.stream(fields).sorted(Comparator.comparingInt(field -> field.getAnnotation(ExcelFormat.class).index()))
                            .collect(LinkedHashMap::new, (map, field) ->
                                            map.put(field.getName(), field.getAnnotation(ExcelFormat.class).headerAlias()),
                                    Map::putAll);
        }
        return null;
    }

}
