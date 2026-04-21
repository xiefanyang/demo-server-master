package com.hnyr.sys.file.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: FileTypeEnum
 * @Description:
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class FileTypeEnum {

    public static final Map<String, String> OTHER = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {

            // xml类型文件
            put(".asa", "application/xml");
            put(".asax", "application/xml");
            put(".ascx", "application/xml");
            put(".ashx", "application/xml");
            put(".asmx", "application/xml");
            put(".aspx", "application/xml");
            put(".config", "application/xml");
            put(".coverage", "application/xml");
            put(".datasource", "application/xml");
            put(".dgml", "application/xml");
            put(".generictest", "application/xml");
            put(".hxa", "application/xml");
            put(".hxc", "application/xml");
            put(".hxe", "application/xml");
            put(".hxf", "application/xml");
            put(".hxk", "application/xml");
            put(".svc", "application/xml");
            put(".rdlc", "application/xml");
            put(".resx", "application/xml");
            put(".ruleset", "application/xml");
            put(".settings", "application/xml");
            put(".snippet", "application/xml");
            put(".testrunconfig", "application/xml");
            put(".testsettings", "application/xml");
            put(".xss", "application/xml");
            put(".xsc", "application/xml");
            put(".hxv", "application/xml");
            put(".loadtest", "application/xml");
            put(".trx", "application/xml");
            put(".psess", "application/xml");
            put(".mtx", "application/xml");
            put(".master", "application/xml");
            put(".orderedtest", "application/xml");
            put(".sitemap", "application/xml");
            put(".skin", "application/xml");
            put(".vscontent", "application/xml");
            put(".vsmdi", "application/xml");
            put(".webtest", "application/xml");
            put(".wiq", "application/xml");
            put(".xmta", "application/xml");

            put(".filters", "Application/xml");
            put(".vcproj", "Application/xml");
            put(".vcxproj", "Application/xml");

            // 文件流类型
            put(".thn", "application/octet-stream");
            put(".toc", "application/octet-stream");
            put(".ttf", "application/octet-stream");
            put(".u32", "application/octet-stream");
            put(".xsn", "application/octet-stream");
            put(".xtp", "application/octet-stream");
            put(".aaf", "application/octet-stream");
            put(".aca", "application/octet-stream");
            put(".afm", "application/octet-stream");
            put(".asd", "application/octet-stream");
            put(".asi", "application/octet-stream");
            put(".cab", "application/octet-stream");
            put(".bin", "application/octet-stream");
            put(".chm", "application/octet-stream");
            put(".cur", "application/octet-stream");
            put(".dat", "application/octet-stream");
            put(".deploy", "application/octet-stream");
            put(".dwp", "application/octet-stream");
            put(".dsp", "application/octet-stream");
            put(".emz", "application/octet-stream");
            put(".eot", "application/octet-stream");
            put(".exe", "application/octet-stream");
            put(".hxd", "application/octet-stream");
            put(".hxh", "application/octet-stream");
            put(".hxi", "application/octet-stream");
            put(".hxq", "application/octet-stream");
            put(".hxr", "application/octet-stream");
            put(".hxs", "application/octet-stream");
            put(".hxw", "application/octet-stream");
            put(".ics", "application/octet-stream");
            put(".hhk", "application/octet-stream");
            put(".hhp", "application/octet-stream");
            put(".inf", "application/octet-stream");
            put(".fla", "application/octet-stream");
            put(".java", "application/octet-stream");
            put(".jpb", "application/octet-stream");
            put(".mdp", "application/octet-stream");
            put(".mix", "application/octet-stream");
            put(".msi", "application/octet-stream");
            put(".mso", "application/octet-stream");
            put(".ocx", "application/octet-stream");
            put(".pcx", "application/octet-stream");
            put(".pcz", "application/octet-stream");
            put(".pfb", "application/octet-stream");
            put(".pfm", "application/octet-stream");
            put(".lzh", "application/octet-stream");
            put(".lpk", "application/octet-stream");
            put(".qxd", "application/octet-stream");
            put(".prm", "application/octet-stream");
            put(".prx", "application/octet-stream");
            put(".psd", "application/octet-stream");
            put(".psm", "application/octet-stream");
            put(".psp", "application/octet-stream");
            put(".rar", "application/octet-stream");
            put(".sea", "application/octet-stream");
            put(".smi", "application/octet-stream");
            put(".snp", "application/octet-stream");


            //
            put(".acx", "application/internet-property-stream");
            put(".ai", "application/postscript");
            put(".atom", "application/atom+xml");
            put(".axs", "application/olescript");
            put(".ustar", "application/x-ustar");
            put(".bcpio", "application/x-bcpio");
            put(".xhtml", "application/xhtml+xml");
            put(".crl", "application/pkix-crl");
            put(".amc", "application/x-mpeg");
            put(".cdf", "application/x-cdf");
            put(".cer", "application/x-x509-ca-cert");
            put(".class", "application/x-java-applet");
            put(".clp", "application/x-msclip");
            put(".application", "application/x-ms-application");
            put(".adobebridge", "application/x-bridge-url");
            put(".cpio", "application/x-cpio");
            put(".crd", "application/x-mscardfile");
            put(".crt", "application/x-x509-ca-cert");
            put(".der", "application/x-x509-ca-cert");
            put(".csh", "application/x-csh");
            put(".dcr", "application/x-director");
            put(".dir", "application/x-director");
            put(".dll", "application/x-msdownload");
            put(".dvi", "application/x-dvi");
            put(".dwf", "drawing/x-dwf");
            put(".dxr", "application/x-director");
            put(".flr", "x-world/x-vrml");
            put(".gtar", "application/x-gtar");
            put(".gz", "application/x-gzip");
            put(".hdf", "application/x-hdf");
            put(".hhc", "application/x-oleobject");
            put(".mmf", "application/x-smaf");
            put(".mny", "application/x-msmoney");
            put(".ms", "application/x-troff-ms");
            put(".mvb", "application/x-msmediaview");
            put(".mvc", "application/x-miva-compiled");
            put(".mxp", "application/x-mmxp");
            put(".nc", "application/x-netcdf");
            put(".pcast", "application/x-podcast");
            put(".ins", "application/x-internet-signup");
            put(".jnlp", "application/x-java-jnlp-file");
            put(".js", "application/x-javascript");
            put(".latex", "application/x-latex");
            put(".lit", "application/x-ms-reader");
            put(".manifest", "application/x-ms-manifest");
            put(".man", "application/x-troff-man");
            put(".me", "application/x-troff-me");
            put(".mfp", "application/x-shockwave-flash");
            put(".pfx", "application/x-pkcs12");
            put(".p7r", "application/x-pkcs7-certreqresp");
            put(".p12", "application/x-pkcs12");
            put(".p7b", "application/x-pkcs7-certificates");
            put(".pma", "application/x-perfmon");
            put(".pmc", "application/x-perfmon");
            put(".pml", "application/x-perfmon");
            put(".pmr", "application/x-perfmon");
            put(".pmw", "application/x-perfmon");
            put(".iii", "application/x-iphone");
            put(".ipa", "application/x-itunes-ipa");
            put(".ipg", "application/x-itunes-ipg");
            put(".ipsw", "application/x-itunes-ipsw");
            put(".isp", "application/x-internet-signup");
            put(".ite", "application/x-itunes-ite");
            put(".itlp", "application/x-itunes-itlp");
            put(".itms", "application/x-itunes-itms");
            put(".itpc", "application/x-itunes-itpc");
            put(".eps", "application/postscript");
            put(".etl", "application/etl");
            put(".evy", "application/envoy");
            put(".fdf", "application/vnd.fdf");
            put(".fif", "application/fractals");
            put(".fsscript", "application/fsharp-script");
            put(".fsx", "application/fsharp-script");
            put(".hlp", "application/winhlp");
            put(".hqx", "application/mac-binhex40");
            put(".hta", "application/hta");
            put(".jck", "application/liquidmotion");
            put(".jcz", "application/liquidmotion");
            put(".library-ms", "application/windows-library+xml");
            put(".mht", "message/rfc822");
            put(".mhtml", "message/rfc822");
            put(".nws", "message/rfc822");
            put(".eml", "message/rfc822");
            put(".oda", "application/oda");
            put(".ods", "application/oleobject");
            put(".osdx", "application/opensearchdescription+xml");
            put(".p10", "application/pkcs10");
            put(".p7c", "application/pkcs7-mime");
            put(".p7m", "application/pkcs7-mime");
            put(".p7s", "application/pkcs7-signature");
            put(".prf", "application/pics-rules");
            put(".ps", "application/postscript");
            put(".psc1", "application/PowerShell");
            put(".pub", "application/x-mspublisher");
            put(".qtl", "application/x-quicktimeplayer");
            put(".rat", "application/rat-file");
            put(".roff", "application/x-troff");
            put(".rtf", "application/rtf");
            put(".safariextz", "application/x-safari-safariextz");
            put(".scd", "application/x-msschedule");
            put(".sdp", "application/sdp");
            put(".searchConnector-ms", "application/windows-search-connector+xml");
            put(".setpay", "application/set-payment-initiation");
            put(".setreg", "application/set-registration-initiation");
            put(".sgimb", "application/x-sgimb");
            put(".sh", "application/x-sh");
            put(".shar", "application/x-shar");
            put(".sit", "application/x-stuffit");
            put(".slupkg-ms", "application/x-ms-license");
            put(".spc", "application/x-pkcs7-certificates");
            put(".spl", "application/futuresplash");
            put(".src", "application/x-wais-source");
            put(".ssm", "application/streamingmedia");
            put(".sv4cpio", "application/x-sv4cpio");
            put(".sv4crc", "application/x-sv4crc");
            put(".swf", "application/x-shockwave-flash");
            put(".t", "application/x-troff");
            put(".tar", "application/x-tar");
            put(".tcl", "application/x-tcl");
            put(".tex", "application/x-tex");
            put(".texi", "application/x-texinfo");
            put(".texinfo", "application/x-texinfo");
            put(".tr", "application/x-troff");
            put(".trm", "application/x-msterminal");
            put(".vsi", "application/ms-vsi");
            put(".vsix", "application/vsix");
            put(".vsto", "application/x-ms-vsto");
            put(".webarchive", "application/x-safari-webarchive");
            put(".WLMP", "application/wlmoviemaker");
            put(".wlpginstall", "application/x-wlpg-detect");
            put(".wlpginstall3", "application/x-wlpg3-detect");
            put(".x", "application/directx");
            put(".xaml", "application/xaml+xml");
            put(".xht", "application/xhtml+xml");
            put(".xap", "application/x-silverlight-app");
            put(".xbap", "application/x-ms-xbap");
            put(".xaf", "x-world/x-vrml");
            put(".xof", "x-world/x-vrml");
            put(".wrl", "x-world/x-vrml");
            put(".wrz", "x-world/x-vrml");

        }
    };

    /**
     * 图片
     */
    public static final Map<String, String> PICS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {

            // 常用图片
            put(".jpe", "image/jpeg");
            put(".jpeg", "image/jpeg");
            put(".jpg", "image/jpeg");
            put(".bmp", "image/bmp");
            put(".png", "image/png");
            put(".gif", "image/gif");

            // 不常用
            put(".jfif", "image/pjpeg");
            put(".dib", "image/bmp");
            put(".pnz", "image/png");
            put(".art", "image/x-jg");
            put(".cmx", "image/x-cmx");
            put(".ico", "image/x-icon");
            put(".ppm", "image/x-portable-pixmap");
            put(".mac", "image/x-macpaint");
            put(".pbm", "image/x-portable-bitmap");
            put(".pgm", "image/x-portable-graymap");
            put(".pnm", "image/x-portable-anymap");
            put(".pnt", "image/x-macpaint");
            put(".pntg", "image/x-macpaint");
            put(".qti", "image/x-quicktime");
            put(".qtif", "image/x-quicktime");
            put(".rgb", "image/x-rgb");
            put(".xwd", "image/x-xwindowdump");
            put(".ras", "image/x-cmu-raster");
            put(".xbm", "image/x-xbitmap");
            put(".xpm", "image/x-xpixmap");
            put(".cod", "image/cis-cod");
            put(".ief", "image/ief");
            put(".pct", "image/pict");
            put(".pic", "image/pict");
            put(".pict", "image/pict");
            put(".rf", "image/vnd.rn-realflash");
            put(".wbmp", "image/vnd.wap.wbmp");
            put(".wdp", "image/vnd.ms-photo");
            put(".tif", "image/tiff");
            put(".tiff", "image/tiff");
        }
    };

    /**
     * 文档
     */
    public static final Map<String, String> DOCS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {

            /** 常用  */

            // txt
            put(".txt", "text/plain");

            // css
            put(".css", "text/css");

            // html
            put(".htm", "text/html");
            put(".html", "text/html");
            put(".shtml", "text/html");

            // xml
            put(".wsdl", "text/xml");
            put(".xml", "text/xml");

            // pdf
            put(".pdf", "application/pdf");

            // ppt
            put(".ppt", "application/vnd.ms-powerpoint");
            put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

            // doc
            put(".doc", "application/msword");
            put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

            // excel
            put(".xlm", "application/vnd.ms-excel");
            put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // note
            put(".one", "application/onenote");

            // access
            put(".accdb", "application/msaccess");

            // visio
            put(".vsd", "application/vnd.visio");


            /** 不常用 */

            put(".323", "text/h323");
            put(".rqy", "text/x-ms-rqy");
            put(".rtx", "text/richtext");
            put(".rc", "text/plain");
            put(".XOML", "text/plain");
            put(".sln", "text/plain");
            put(".rgs", "text/plain");
            put(".pkgdef", "text/plain");
            put(".pkgundef", "text/plain");
            put(".sol", "text/plain");
            put(".sor", "text/plain");
            put(".srf", "text/plain");
            put(".xdr", "text/plain");
            put(".rc2", "text/plain");
            put(".rct", "text/plain");
            put(".s", "text/plain");
            put(".asm", "text/plain");
            put(".c", "text/plain");
            put(".cc", "text/plain");
            put(".cd", "text/plain");
            put(".def", "text/plain");
            put(".cxx", "text/plain");
            put(".cnf", "text/plain");
            put(".cpp", "text/plain");
            put(".cs", "text/plain");
            put(".csdproj", "text/plain");
            put(".csproj", "text/plain");
            put(".dbproj", "text/plain");
            put(".bas", "text/plain");
            put(".dsw", "text/plain");
            put(".inc", "text/plain");
            put(".hxx", "text/plain");
            put(".i", "text/plain");
            put(".idl", "text/plain");
            put(".inl", "text/plain");
            put(".lst", "text/plain");
            put(".jsxbin", "text/plain");
            put(".mak", "text/plain");
            put(".map", "text/plain");
            put(".h", "text/plain");
            put(".hpp", "text/plain");
            put(".ipproj", "text/plain");
            put(".mk", "text/plain");
            put(".odh", "text/plain");
            put(".odl", "text/plain");
            put(".tsv", "text/tab-separated-values");
            put(".uls", "text/iuls");
            put(".user", "text/plain");
            put(".tlh", "text/plain");
            put(".tli", "text/plain");
            put(".vb", "text/plain");
            put(".vbdproj", "text/plain");
            put(".vbproj", "text/plain");
            put(".vcs", "text/plain");
            put(".vddproj", "text/plain");
            put(".vdp", "text/plain");
            put(".vdproj", "text/plain");
            put(".vspscc", "text/plain");
            put(".vsscc", "text/plain");
            put(".vssscc", "text/plain");

            put(".hxt", "text/html");

            put(".vssettings", "text/xml");
            put(".vstemplate", "text/xml");
            put(".vml", "text/xml");
            put(".vsct", "text/xml");
            put(".vsixlangpack", "text/xml");
            put(".vsixmanifest", "text/xml");
            put(".exe.config", "text/xml");
            put(".disco", "text/xml");
            put(".dll.config", "text/xml");
            put(".AddIn", "text/xml");
            put(".dtd", "text/xml");
            put(".dtsConfig", "text/xml");
            put(".mno", "text/xml");
            put(".xrm-ms", "text/xml");
            put(".xsd", "text/xml");
            put(".xsf", "text/xml");
            put(".xsl", "text/xml");
            put(".xslt", "text/xml");
            put(".SSISDeploymentManifest", "text/xml");

            put(".iqy", "text/x-ms-iqy");
            put(".contact", "text/x-ms-contact");
            put(".etx", "text/x-setext");
            put(".hdml", "text/x-hdml");
            put(".htc", "text/x-component");
            put(".group", "text/x-ms-group");
            put(".vcf", "text/x-vcard");
            put(".odc", "text/x-ms-odc");
            put(".qht", "text/x-html-insertion");
            put(".qhtm", "text/x-html-insertion");

            put(".wml", "text/vnd.wap.wml");
            put(".wmls", "text/vnd.wap.wmlscript");

            put(".vbs", "text/vbscript");
            put(".jsx", "text/jscript");
            put(".sct", "text/scriptlet");
            put(".csv", "text/csv");

            put(".323", "text/h323");
            put(".dlm", "text/dlm");
            put(".htt", "text/webviewhtml");
            put(".wsc", "text/scriptlet");
            put(".sgml", "text/sgml");

            // ppt
            put(".pot", "application/vnd.ms-powerpoint");
            put(".ppa", "application/vnd.ms-powerpoint");
            put(".pwz", "application/vnd.ms-powerpoint");
            put(".pps", "application/vnd.ms-powerpoint");
            put(".sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
            put(".ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
            put(".potm", "application/vnd.ms-powerpoint.template.macroEnabled.12");
            put(".ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
            put(".pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
            put(".potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
            put(".ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");

            // doc
            put(".wbk", "application/msword");
            put(".wiz", "application/msword");
            put(".dot", "application/msword");
            put(".docm", "application/vnd.ms-word.document.macroEnabled.12");
            put(".dotm", "application/vnd.ms-word.template.macroEnabled.12");
            put(".dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");

            // excel
            put(".xla", "application/vnd.ms-excel");
            put(".xlc", "application/vnd.ms-excel");
            put(".xld", "application/vnd.ms-excel");
            put(".xlk", "application/vnd.ms-excel");
            put(".xll", "application/vnd.ms-excel");
            put(".xls", "application/vnd.ms-excel");
            put(".xlt", "application/vnd.ms-excel");
            put(".xlw", "application/vnd.ms-excel");
            put(".slk", "application/vnd.ms-excel");
            put(".xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
            put(".xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
            put(".xltm", "application/vnd.ms-excel.template.macroEnabled.12");
            put(".xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
            put(".xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");

            // access
            put(".accde", "application/msaccess");
            put(".accdt", "application/msaccess");
            put(".adp", "application/msaccess");
            put(".mda", "application/msaccess");
            put(".mde", "application/msaccess");
            put(".accda", "application/msaccess.addin");
            put(".accdc", "application/msaccess.cab");
            put(".accdr", "application/msaccess.runtime");
            put(".accdw", "application/msaccess.webapplication");
            put(".accft", "application/msaccess.ftemplate");
            put(".ade", "application/msaccess");


            // visio
            put(".thmx", "application/vnd.ms-officetheme");
            put(".vdx", "application/vnd.ms-visio.viewer");
            put(".vss", "application/vnd.visio");
            put(".vst", "application/vnd.visio");
            put(".vsw", "application/vnd.visio");
            put(".vsx", "application/vnd.visio");
            put(".vtx", "application/vnd.visio");

            // note类型文本
            put(".onea", "application/onenote");
            put(".onepkg", "application/onenote");
            put(".onetmp", "application/onenote");
            put(".onetoc", "application/onenote");
            put(".onetoc2", "application/onenote");

            // 其他
            put(".pko", "application/vnd.ms-pki.pko");
            put(".cat", "application/vnd.ms-pki.seccat");
            put(".sst", "application/vnd.ms-pki.certstore");
            put(".stl", "application/vnd.ms-pki.stl");
            put(".mpf", "application/vnd.ms-mediapackage");
            put(".mpp", "application/vnd.ms-project");
            put(".wpl", "application/vnd.ms-wpl");
            put(".wks", "application/vnd.ms-works");
            put(".wps", "application/vnd.ms-works");
            put(".wcm", "application/vnd.ms-works");
            put(".wdb", "application/vnd.ms-works");
            put(".calx", "application/vnd.ms-office.calx");
            put(".xps", "application/vnd.ms-xpsdocument");
            put(".odp", "application/vnd.oasis.opendocument.presentation");
            put(".odt", "application/vnd.oasis.opendocument.text");
            put(".rm", "application/vnd.rn-realmedia");
            put(".rmp", "application/vnd.rn-rn_music_package");
            put(".sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
            put(".air", "application/vnd.adobe.air-application-installer-package+zip");
            put(".wmlsc", "application/vnd.wap.wmlscriptc");
            put(".wmlc", "application/vnd.wap.wmlc");

            put(".m13", "application/x-msmediaview");
            put(".m14", "application/x-msmediaview");
            put(".wmf", "application/x-msmetafile");
            put(".wri", "application/x-mswrite");
            put(".mdb", "application/x-msaccess");
            put(".wmd", "application/x-ms-wmd");
            put(".wmz", "application/x-ms-wmz");

        }
    };

    /**
     * 压缩文档
     */
    public static final Map<String, String> ZIPDOCS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            // 常用
            put(".7z", "application/x-7z-compressed");
            put(".z", "application/x-compress");
            put(".zip", "application/x-zip-compressed");
            put(".tgz", "application/x-compressed");
            put(".jar", "application/java-archive");
            put(".rar", "application/octet-stream");
            // 不常用
        }
    };

    /**
     * 视频
     */
    public static final Map<String, String> VIDEOS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            // 常用
            put(".flv", "video/x-flv");
            put(".3gp", "video/3gpp");
            put(".avi", "video/x-msvideo");
            put(".mp4", "video/mp4");

            // 不常用
            put(".3g2", "video/3gpp2");
            put(".3gp2", "video/3gpp2");
            put(".3gpp", "video/3gpp");
            put(".asf", "video/x-ms-asf");
            put(".asr", "video/x-ms-asf");
            put(".asx", "video/x-ms-asf");
            put(".dif", "video/x-dv");
            put(".mod", "video/mpeg");
            put(".mov", "video/quicktime");
            put(".movie", "video/x-sgi-movie");
            put(".mp2", "video/mpeg");
            put(".mp2v", "video/mpeg");
            put(".dv", "video/x-dv");
            put(".IVF", "video/x-ivf");
            put(".lsf", "video/x-la-asf");
            put(".lsx", "video/x-la-asf");
            put(".m1v", "video/mpeg");
            put(".m2t", "video/vnd.dlna.mpeg-tts");
            put(".m2ts", "video/vnd.dlna.mpeg-tts");
            put(".m2v", "video/mpeg");
            put(".m4v", "video/x-m4v");
            put(".mp4v", "video/mp4");
            put(".mpa", "video/mpeg");
            put(".mpe", "video/mpeg");
            put(".mpeg", "video/mpeg");
            put(".wm", "video/x-ms-wm");
            put(".mpg", "video/mpeg");
            put(".mpv2", "video/mpeg");
            put(".mqv", "video/quicktime");
            put(".nsc", "video/x-ms-asf");
            put(".qt", "video/quicktime");
            put(".ts", "video/vnd.dlna.mpeg-tts");
            put(".vbk", "video/mpeg");
            put(".wmp", "video/x-ms-wmp");
            put(".wmv", "video/x-ms-wmv");
            put(".wmx", "video/x-ms-wmx");
            put(".wvx", "video/x-ms-wvx");
            put(".mts", "video/vnd.dlna.mpeg-tts");
            put(".tts", "video/vnd.dlna.mpeg-tts");
        }
    };

    /**
     * 音频
     */
    public static final Map<String, String> AUDIOS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            // 常用
            put(".mp3", "audio/mpeg");
            put(".wma", "audio/x-ms-wma");

            // 不常用
            put(".aa", "audio/audible");
            put(".AAC", "audio/aac");
            put(".aax", "audio/vnd.audible.aax");
            put(".ac3", "audio/ac3");
            put(".ADT", "audio/vnd.dlna.adts");
            put(".ADTS", "audio/aac");
            put(".aif", "audio/x-aiff");
            put(".aifc", "audio/aiff");
            put(".aiff", "audio/aiff");
            put(".cdda", "audio/aiff");
            put(".au", "audio/basic");
            put(".m3u", "audio/x-mpegurl");
            put(".m3u8", "audio/x-mpegurl");
            put(".m4a", "audio/m4a");
            put(".m4b", "audio/m4b");
            put(".m4p", "audio/m4p");
            put(".m4r", "audio/x-m4r");
            put(".caf", "audio/x-caf");
            put(".gsm", "audio/x-gsm");
            put(".mid", "audio/mid");
            put(".midi", "audio/mid");
            put(".pls", "audio/scpls");
            put(".ra", "audio/x-pn-realaudio");
            put(".ram", "audio/x-pn-realaudio");
            put(".rmi", "audio/mid");
            put(".rpm", "audio/x-pn-realaudio-plugin");
            put(".sd2", "audio/x-sd2");
            put(".smd", "audio/x-smd");
            put(".smx", "audio/x-smd");
            put(".smz", "audio/x-smd");
            put(".snd", "audio/basic");
            put(".wav", "audio/wav");
            put(".wave", "audio/wav");
            put(".wax", "audio/x-ms-wax");
        }
    };

}
