package com.jerry.up.lala.framework.core.data;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import com.jerry.up.lala.framework.core.common.CommonConstant;
import com.jerry.up.lala.framework.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Description: bean 操作
 *
 * @author FMJ
 * @date 2023/10/25 13:22
 */
@Slf4j
public class DataUtil {

    public static <S, T> T toBean(S source, Class<T> targetType, String... targetTypeIgnoreProperties) {
        if (source == null) {
            return null;
        }
        // 原注解非空，且不包含目标类
        DataBean dataBean = AnnotationUtil.getAnnotationValue(source.getClass(), DataBean.class);
        if (dataBean != null && ArrayUtil.isNotEmpty(dataBean.targetTypes())
                && !ArrayUtil.contains(dataBean.targetTypes(), targetType)) {
            throw ServiceException.error();
        }
        try {
            T target = BeanUtil.toBean(source, targetType, CopyOptions.create().setIgnoreProperties(targetTypeIgnoreProperties).setIgnoreError(true));
            beanFormat(source, target);
            return target;
        } catch (Exception e) {
            log.error("转换属性异常", e);
        }
        return null;
    }

    public static <S, T> void copy(S source, T target, String... targetTypeIgnoreProperties) {
        if (source == null) {
            return;
        }
        // 原注解非空，且不包含目标类
        DataBean dataBean = AnnotationUtil.getAnnotationValue(source.getClass(), DataBean.class);
        if (dataBean != null && ArrayUtil.isNotEmpty(dataBean.targetTypes())
                && !ArrayUtil.contains(dataBean.targetTypes(), target.getClass())) {
            throw ServiceException.error();
        }
        try {
            BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreProperties(targetTypeIgnoreProperties).setIgnoreError(true));
            beanFormat(source, target);
        } catch (Exception e) {
            log.error("转换属性异常", e);
        }
    }

    public static <D> void beanFormat(D data) {
        try {
            if (data != null) {
                beanFormat(data, data);
            }
        } catch (Exception e) {
            log.error("格式化属性异常", e);
        }
    }

    public static <S, T> List<T> toBeanList(List<S> sourceList, Class<T> targetType, String... targetTypeIgnoreProperties) {
        if (CollectionUtil.isEmpty(sourceList)) {
            return null;
        }
        return sourceList.stream().map(source -> toBean(source, targetType, targetTypeIgnoreProperties)).collect(Collectors.toList());
    }

    public static <D> void beanFormatList(List<D> dataList) {
        if (dataList != null) {
            dataList.forEach(DataUtil::beanFormat);
        }
    }

    public static boolean clearBooleanNull(Boolean bool, Boolean defaultValue) {
        return defaultValue ? (bool == null || bool) : (bool != null && bool);
    }

    private static <S, T> void beanFormat(S source, T target) {
        Field[] targetFieldList = ReflectUtil.getFields(target.getClass(), item -> item.getAnnotation(DataFormat.class) != null);
        if (ArrayUtil.isEmpty(targetFieldList)) {
            return;
        }
        Arrays.stream(targetFieldList).forEach(targetField -> {
            DataFormat dataFormat = targetField.getAnnotation(DataFormat.class);
            String targetFieldName = targetField.getName();
            if (dataFormat != null) {
                String sourceFieldName = StringUtil.isNotNull(dataFormat.sourceFieldName()) ? dataFormat.sourceFieldName() : targetFieldName;
                Field sourceField = ReflectUtil.getField(source.getClass(), sourceFieldName);
                if (sourceField != null) {
                    Object sourceValue = ReflectUtil.getFieldValue(source, sourceField);
                    if (sourceValue != null) {
                        boolean sourceString = sourceValue instanceof String;
                        boolean sourceList = sourceValue instanceof List;

                        int maxLength = dataFormat.maxLength();
                        if (sourceString && maxLength > 0) {
                            sourceValue = StrUtil.sub(sourceValue.toString(), 0, maxLength);
                        }

                        // 字符串 转为list 分隔符
                        String split = dataFormat.split();
                        if (StringUtil.isNotNull(split)) {
                            if (sourceString) {
                                sourceValue = strToList(sourceValue, split, TypeUtil.getTypeArgument(TypeUtil.getType(targetField)));
                            }
                            if (sourceList) {
                                sourceValue = listToStr(sourceValue, split);
                            }
                        }
                        // list 获取 索引元素
                        int listIndex = dataFormat.listIndex();
                        if (listIndex >= 0) {
                            if (sourceList) {
                                sourceValue = listIndex(sourceValue, listIndex);
                            }
                        }
                        // 时间格式转换
                        int dateType = dataFormat.dateType();
                        if (dateType >= 0) {
                            if (sourceValue instanceof String) {
                                sourceValue = strToDate(sourceValue, dateType);
                            }
                            if (sourceValue instanceof List) {
                                sourceValue = ((List) sourceValue).stream().map(item -> strToDate(item, dateType)).collect(Collectors.toList());
                            }
                        }
                    }
                    try {
                        ReflectUtil.setFieldValue(target, targetField, sourceValue);
                    } catch (Exception e) {
                        log.error("赋值参数异常, sourceFieldName {}, targetFieldName {} ", sourceFieldName, targetFieldName, e);
                    }
                }
            }
        });
    }

    private static List strToList(Object sourceValue, String strToListSplit, Type type) {
        if (StringUtil.isNull((String) sourceValue)) {
            return null;
        }
        List result = new ArrayList();
        Stream.of(((String) sourceValue).split(strToListSplit)).forEach(item -> {
            Object valueOf = item;
            if (type != null) {
                String typeName = type.getTypeName();
                if (Integer.class.getName().equals(typeName)) {
                    valueOf = Integer.valueOf(item);
                } else if (Long.class.getName().equals(typeName)) {
                    valueOf = Long.valueOf(item);
                } else if (Double.class.getName().equals(typeName)) {
                    valueOf = Double.valueOf(item);
                }
            }
            result.add(valueOf);
        });
        return result;
    }

    private static String listToStr(Object sourceValue, String split) {
        if (CollectionUtil.isEmpty((List) sourceValue)) {
            return null;
        }
        return StrUtil.join(split, (List) sourceValue);
    }

    private static Object listIndex(Object sourceValue, Integer listIndex) {
        if (CollectionUtil.isEmpty((List) sourceValue)) {
            return null;
        }
        if (((List) sourceValue).size() <= listIndex) {
            return null;
        }
        return ((List) sourceValue).get(listIndex);
    }

    private static Date strToDate(Object sourceValue, Integer dateType) {
        String dateStr = ObjectUtil.toString(sourceValue);
        if (StringUtil.isNull(dateStr)) {
            return null;
        }
        if (CommonConstant.DATE_TYPE_DATE.equals(dateType)) {
            return DateUtil.parse(dateStr, "yyyy-MM-dd");
        }
        if (CommonConstant.DATE_TYPE_TIME.equals(dateType)) {
            return DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }

    public static List<Tree<String>> toTreeBySeparator(Set<String> keys, String separator) {
        List<TreeNode<String>> list = new ArrayList<>();
        keys.forEach(key -> {
            List<String> array = StrUtil.split(key, separator);
            for (int i = 0; i < array.size(); i++) {
                if (i == 0) {
                    list.add(new TreeNode<>(array.get(0) + separator, "", array.get(0), null));
                } else if (i == array.size() - 1) {
                    Map<String, Object> extra = MapUtil.newHashMap();
                    extra.put("key", true);
                    list.add(new TreeNode<>(key, arrayStr(array, i - 1, separator), StrUtil.subAfter(key, separator, true), null).setExtra(extra));
                } else {
                    String treeKey = arrayStr(array, i, separator);
                    String name = StrUtil.subAfter(StringUtil.removeSuffix(treeKey, separator), separator, true);
                    list.add(new TreeNode<>(treeKey, arrayStr(array, i - 1, separator), name, null));
                }
            }
        });
        return TreeUtil.build(list, "");
    }

    private static String arrayStr(List<String> array, int index, String separator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            result.append(array.get(i)).append(separator);
        }
        return result.toString();
    }

    public static <ID, D> List<Tree<ID>> buildTree(List<D> dataList, Function<D, TreeNode<ID>> mapper, ID rootId) {
        if (CollectionUtil.isEmpty(dataList)) {
            return new ArrayList<>();
        }
        List<TreeNode<ID>> treeNodeList = dataList.stream().map(mapper).collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, rootId);
    }

    public static <ID, V> List<V> getParents(Tree<ID> node, Function<Tree<ID>, V> mapper, boolean includeCurrentNode, ID rootId) {
        final List<V> result = new ArrayList<>();
        if (null == node) {
            return result;
        }

        if (includeCurrentNode) {
            result.add(mapper.apply(node));
        }

        Tree<ID> parent = node.getParent();
        while (null != parent) {
            if (ObjectUtil.notEqual(rootId, parent.getId())) {
                result.add(mapper.apply(parent));
            }
            parent = parent.getParent();
        }
        return CollectionUtil.reverse(result);
    }

    public static String valuesStr(String key, Map<String, String> map) {
        if (StringUtil.isNull(key)) {
            return null;
        }
        return Arrays.stream(key.replace("，", ",").split(","))
                .map(map::get).filter(StringUtil::isNotNull).collect(Collectors.joining(","));
    }
}
