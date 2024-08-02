<table cellspacing="0" cellpadding="0" width="100%"
       name="tblText" class="tblText" border="0">
    <tbody>
    <tr>
        <td>
            <div>
                <strong><span class="span_style">请求方式：</span></strong>${servletMethod}
            </div>
            <div>
                <strong><span class="span_style">请求URL：</span></strong>${requestUrlInfo}
            </div>

            <#if requestBody??>
                <#if requestBody != "">
                    <div>
                        <strong><span class="span_style">请求参数(requestBody)：</span></strong>
                        <pre class="pre_code">${requestBody}</pre>
                    </div>
                </#if>
            </#if>

            <div>
                <strong><span class="span_style">用户信息：</span></strong>${userInfo}
            </div>
            <div>
                <strong><span class="span_style">客户端平台：</span></strong>${userAgent}
            </div>
            <div>
                <strong><span class="span_style">客户端信息：</span></strong>${clientInfo}
            </div>
            <div>
                <strong><span class="span_style">服务端信息：</span></strong>${serverInfo}
            </div>
            <div>
                <strong><span class="span_style">异常时间：</span></strong>${date}
            </div>
            <div>
                <strong><span class="span_style">异常级别：</span></strong>${exceptionLevel}
            </div>
            <div>
                <strong><span class="span_style">异常code：</span></strong>${errorCode}
            </div>
            <div>
                <strong><span class="span_style">异常信息：</span></strong>${errorMsg}
            </div>
            <div>
                <strong><span class="span_style">捕获异常：</span></strong>${catchStackTrace}
            </div>
            <div>
                <strong><span class="span_style">业务异常：</span></strong>${serviceStackTrace}
            </div>
            <#if runTimeStackTrace??>
                <#if runTimeStackTrace != "">
                    <div>
                        <strong><span class="span_style">运行时异常：</span></strong>${runTimeStackTrace}
                    </div>
                </#if>
            </#if>
        </td>
    </tr>
    </tbody>
</table>

<style>
    .span_style {
        font-size: 16px;
        color: #3b3b3b;
        line-height: 200%;
    }

    .pre_code {
        font-family: monospace;
        font-size: 14px;
        border: 1px solid #e2e8e8;
        margin: 0 0 1em 0;
        background: #f7f7f7;
        position: relative;
    }

</style>