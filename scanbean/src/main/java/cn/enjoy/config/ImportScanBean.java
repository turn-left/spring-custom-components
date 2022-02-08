package cn.enjoy.config;

import org.springframework.context.annotation.Import;

/**
 * @Classname ImportScanBean
 * @Description TODO
 * @Author Jack
 * Date 2021/1/10 20:40
 * Version 1.0
 */
@Import({BeansScannerImportSelector.class})
//@BeansScaner("com.xiangxue.jack")
public class ImportScanBean {
}
