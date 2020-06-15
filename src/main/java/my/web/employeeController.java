package my.web;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import my.afspring.AfRestData;
import my.domain.QueryParameter;
import my.Util.RestfulResult;
import my.domain.dataGridResult;
import my.domain.department;
import my.domain.employee;
import my.service.employeeService;
import my.Util.ResponseJsonResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Scope("prototype")//在WEB开发中我们尽量把控制器设置为多例的
public class employeeController {
    @Autowired
    /*注入业务层--必须是业务层的接口不可以是业务层的实现类*/
    public employeeService employeeService;

    /*获取员工页面*/
    @RequestMapping("/employee.action")
    /*该注解表示访问某个控制器处理方法时，必须有某个，或某几个权限
    （具体需要的权限 由该注解值决定）
    --记录*/
    @RequiresPermissions("index:Visit")  /*xx:xx --表示对某个资源，有什么样的权限*/
    public Object getemployeePage() {

        return "employee";
    }

    /*获取所有的员工信息*/
    @RequestMapping("/getemployeeList.action")
    @ResponseBody/*信息转化--可以将应答返回的对象（pojo,map,list,json）转为JSON字段串*/
    public Object getemployeeList(QueryParameter queryParameter) {
        System.out.println("查询");
        System.out.println(queryParameter);
        dataGridResult result = employeeService.getallemployee(queryParameter);
        return result;
    }

    /*添加员工*/
    @RequestMapping("/savaEmployee.action")
    @RequiresPermissions("employee:add")
    @ResponseBody
    public Object savaEmployee(employee employee) {
        System.out.println("--------------添加员工---------------------");
        System.out.println(employee);
        employee.setState(true);
        //将密码进行Md5加密
        Md5Hash md5password = new Md5Hash(employee.getPassword(), "xia", 2);
        employee.setPassword(md5password.toString());
        if (employee.getAdmin() == null) {

            employee.setAdmin(false);
        }
        RestfulResult restfulResult = new RestfulResult();
        try {
            employeeService.addEmployee(employee);
            restfulResult.setData("保存成功");
            return restfulResult;
        } catch (Exception e) {
            restfulResult.setData("保存失败");
            return restfulResult;

        }
    }

    /*编辑更新员工信息*/
    @RequestMapping("/editEmployee.action")
    @RequiresPermissions("employee:edit")
    @ResponseBody
    public Object editEmployee(employee employee) {
        System.out.println("编辑操作");
        System.out.println(employee);
        //将密码进行Md5加密
        Md5Hash md5password = new Md5Hash(employee.getPassword(), "xia", 2);
        employee.setPassword(md5password.toString());

        RestfulResult restfulResult = new RestfulResult();
        try {
            employeeService.EditEmployee(employee);
            restfulResult.setData("编辑成功");
            return restfulResult;
        } catch (Exception e) {
            System.out.println("ExceptionMessgae:" + e);
            restfulResult.setData("编辑失败");
            return restfulResult;

        }
    }

    /*设置离职状态*/
    @RequestMapping("/setState.action")
    @ResponseBody
    @RequiresPermissions("employee:edit")
    public Object setState(@RequestBody JSONObject jsonObject) {
        Integer id = Integer.valueOf(jsonObject.getString("id"));
        System.out.println(id);
        RestfulResult restfulResult = new RestfulResult();
        try {
            employeeService.updateState(id);
            restfulResult.setData("设置离职状态成功");
            return restfulResult;

        } catch (Exception e) {
            System.out.println("ExceptionMessgae:" + e);
            restfulResult.setData("设置离职状态失败");
            return restfulResult;

        }
    }

    /*Excel导出*/
    @RequestMapping("/ExcelOut.action")
    public Object ExcelOut(HttpServletResponse response) throws IOException {
        System.out.println("Excel导出");

        /*1 从数据库中查询表格的数据
         * 2 创建Excel  把表格数据写到Excel中
         * 3 应答返回数据给客户端（文件的流）
         * */

        /*1 从数据库中查询表格的数据*/
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.setPage(1);
        queryParameter.setRows(50);
        dataGridResult getallemployee = employeeService.getallemployee(queryParameter);
        List<employee> employeelist = (List<employee>) getallemployee.getRows();

        /*2  创建Excel  把表格数据写到Excel中*/
        HSSFWorkbook wb = new HSSFWorkbook();
        /*创建一个Excel的页 （Excel中可以有很多页）  Sheet--表示Excel的页 */
        HSSFSheet sheet = wb.createSheet("员工数据");
        /*创建Excel页的第一行*/
        HSSFRow row = sheet.createRow(0);
        /*创建第一行中---并设置第一行所有列的数据（写死的）*/
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("用户名");
        row.createCell(2).setCellValue("入职日期");
        row.createCell(3).setCellValue("电话");
        row.createCell(4).setCellValue("邮件");
        row.createCell(5).setCellValue("部门");

        HSSFRow sheetRow;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        /*遍历所有的员工，把一个员工的数据设置到Excel中*/
        for (int i = 0; i < employeelist.size(); i++) {
            employee employee = employeelist.get(i);
            /*注意要从第二行开始设置数据*/
            sheetRow = sheet.createRow(i + 1);
            sheetRow.createCell(0).setCellValue(employee.getId());
            sheetRow.createCell(1).setCellValue(employee.getUsername());
            if (employee.getInputtime() != null) {
                sheetRow.createCell(2).setCellValue(simpleDateFormat.format(employee.getInputtime()));
            } else {
                sheetRow.createCell(2).setCellValue("");
            }
            sheetRow.createCell(3).setCellValue(employee.getTel());
            sheetRow.createCell(4).setCellValue(employee.getEmail());
            if (employee.getDepartment() != null) {
                sheetRow.createCell(5).setCellValue(employee.getDepartment().getName());
            } else {
                sheetRow.createCell(5).setCellValue("");
            }
        }

        /*3 应答返回数据给客户端（文件的流）*/
        /*设置文件名---中文的文件名要进行utf-8编码*/
        String fileName = new String("员工数据2.xls".getBytes("utf-8"), "iso8859-1");
        // 设置响应头
        // content-Disposition 告诉浏览器以附件的形式下载文件
        response.setHeader("content-Disposition", "attachment;filename=" + fileName);
        /*将HSSFWorkbook（Excel）的（数据源）写入到应答返回数据输出流*/
        wb.write(response.getOutputStream());
        return null;
    }

    /*模板文件下载--下载模板Excel文件*/
    @RequestMapping("/downLoadtml.action")
    public Object downLoadtml(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File webroot = new File(request.getServletContext().getRealPath("/"));
        System.out.println("webroot:" + webroot);
        //目标下载文件
        File file = new File(webroot, "/static/模板文件.xls");
        //获取目标文件的数据源（输入流）
        FileInputStream fileInputStream = new FileInputStream(file);
        System.out.println("O流的长度即字节数B" + fileInputStream.available());

        /*设置文件名---中文的文件名要进行utf-8编码*/
        String fileName = new String("模板文件.xls".getBytes("utf-8"), "iso8859-1");
        // 设置响应头
        // content-Disposition 告诉浏览器以附件的形式下载文件
        response.setHeader("content-Disposition", "attachment;filename=" + fileName);
        // 第一种方法：把目标文件的数据源读取到应答返回数据的输出流中
        readFile(response.getOutputStream(), fileInputStream, file);
        /*第二种方法-把目标文件中的数据源读取到应答返回数据的输出流中*/
        // IOUtils.copy(fileInputStream,response.getOutputStream());
        fileInputStream.close();
        return null;
    }

     /*Excel文件导入*/
    /*以后后端的文件上传代码我们尽量用高新强的否则在Maven项目中会出错*/
    //文件上传--高新强的方式（不管是多文件上传还是单文件上传后台代码都是一样的）
    @RequestMapping("/TestsimpleFileUpload.action")
    @ResponseBody
    //@RequestParam("file") CommonsMultipartFile file  将请求正文中的参数名为file参数赋值给这个CommonsMultipartFile
    public Object TestsimpleFileUpload(@RequestParam("file") CommonsMultipartFile MultipartFile, javax.servlet.http.HttpServletRequest request) throws IOException {
        System.out.println("表单中的name参数值：" + MultipartFile.getName());
        System.out.println("文件名：" + MultipartFile.getOriginalFilename());
        System.out.println(MultipartFile.getContentType());
        System.out.println(MultipartFile.getSize());
        RestfulResult restfulResult = new RestfulResult();
        //获取上传文件的后缀
        String suffix = getSuffix(MultipartFile.getOriginalFilename());
        System.out.println(suffix);
        /*获取web项目绝对路径即webroot目录路径*/
        File webroot=new File(request.getServletContext().getRealPath("/"));;

        /*生成唯一的目录名*/
        String   foldername= getuuid();
        // 临时目录（在webroot目录下创建的临时目录）---上传文件暂时存放的地方
        File tmpdir = new File(webroot, "static/Exceltmp"+foldername);

        // 如果没有就自动生成
        tmpdir.mkdirs();
        //文件存储路径（存储在临时目录中）
        File tmpFile = new File(tmpdir, MultipartFile.getOriginalFilename());
        System.out.println(tmpFile);
        //开始上传。接受上传
        // 把上传的文件数据传输到临时文件路径中，从而生成临时文件
        //转存文件到指定的文件或路径中。）
        MultipartFile.transferTo(tmpFile);
        FileInputStream fileInputStream = new FileInputStream(tmpFile);
        try {
            restfulResult.setData("导入成功");
            //利用上传文件的数据源去创建HSSFWorkbook对象--即工作簿（Excel）对象
            HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
            new HSSFWorkbook();
            /*获取Excel的第一页*/
            HSSFSheet sheet = wb.getSheetAt(0);
            /*获取Excel的第一页最大的行号*/
            int lastRowNum = sheet.getLastRowNum();
            System.out.println("获取最大的行号" + lastRowNum);
            HSSFRow sheetRow;
            for (int i = 1; i <= lastRowNum; i++) {
                sheetRow = sheet.getRow(i);
                employee employee = new employee();
                /*主键字段会自动生成不必我们自己插入数据*/
                //getCellValue(sheetRow.getCell(0));
                employee.setUsername((String) getCellValue(sheetRow.getCell(1)));
                employee.setInputtime((Date) getCellValue(sheetRow.getCell(2)));
                employee.setTel(String.valueOf( getCellValue(sheetRow.getCell(3))));
                employee.setEmail(String.valueOf(getCellValue(sheetRow.getCell(4))));
                department department = new department();
                department.setName((String) getCellValue(sheetRow.getCell(5)));
                /*根据部门名字获取部门id*/
                Long id= employeeService.getDepartmentid((String) getCellValue(sheetRow.getCell(5)));
                department.setId(id);
                employee.setDepartment(department);
                employeeService.OnlyaddEmployee(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
            restfulResult.setData("导入失败");
        }
        return restfulResult;
    }


    /*------------没有权限时的异常处理-----------*/
    /*用Springmvc中的异常处理器去处理异常  --这里捕获的是 授权异常（即当前用户没有某个权限）*/
    @ExceptionHandler(AuthorizationException.class)
    public void handleShiroExcepiton(HandlerMethod method, HttpServletResponse response) throws IOException {/*HandlerMethod  出现异常的控制器处理方法*/
      /*先判断当前的请求是否是一个Ajax请求（Ajax请求）（Ajax请求的话我们不能在后端控制页面跳转只能在前端控制页面跳转）--记录
       如果是Ajax请求则应答返回一个JSON字符串  --如果是普通的post或Get请求 则在后端可以控制页面跳转*/
        /*看是否有 @ResponseBody 注解 就可以了*/
        ResponseBody methodAnnotation = method.getMethodAnnotation(ResponseBody.class);
        if (methodAnnotation != null) {
            ResponseJsonResult.responseJsonResult(response, "您没有权限进行该操作");

        } else {
            response.sendRedirect("nopermission.jsp");
        }
    }




    // 获取文件后缀--根据文件名
    public static String getSuffix(String filePath) {
        int p1 = filePath.lastIndexOf('.');
        if (p1 > 0) {
            String suffix = filePath.substring(p1);
            if (suffix.length() < 10) // 后缀长度必须小于10
            {
                // 后缀中不能有路径分隔符
                if (suffix.indexOf('/') < 0 && suffix.indexOf('\\') < 0)
                    return suffix.toLowerCase();
            }
        }
        return "";
    }

    // 生成唯一的文件名（即文件前缀唯一即可）--提供文件后缀即可---记录
    private String makeFileName(String filename) { // 2.jpg
        // 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        //
        return UUID.randomUUID().toString() + "_" + filename;
    }

    //生成唯一的目录名
    // 生成唯一的存储路径(目录)名（所有有关这条帖子的图片都放到该目录下）
    public String getuuid() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String now = sdf.format(new Date());
        return now + UUID.randomUUID().toString() + "/";
    }

    // 把目标文件中的数据读取到应答返回数据的输出流中
    public void readFile(OutputStream o, FileInputStream i, File targetfile) throws FileNotFoundException {
        int size = (int) targetfile.length();// 得到源文件(目标)的长度
        // 文件只接受字节数组
        byte data[] = new byte[size];// 根据源文件的长度得到 byte数组的长度 用它来读取接收src中的数据和
        // 把数据写入dsc
        try {
            i.read(data);// 读取文件输入流即数据源中的数据读取到data中
            o.write(data);// 把 数据源中的数据写入到应答返回数据输出流中
            // 关闭输出输入流
            o.close();
            i.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /*配置文件上传解析器  mvc配置当中*/
    @RequestMapping("/uploadExcelFile.action")
    @ResponseBody
    public Object uploadExcelFile(MultipartFile file) {
        RestfulResult restfulResult = new RestfulResult();
        try {
            restfulResult.setData("导入成功");
            HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
            new HSSFWorkbook();
            /*获取Excel的第一页*/
            HSSFSheet sheet = wb.getSheetAt(0);
            /*获取Excel的第一页最大的行号*/
            int lastRowNum = sheet.getLastRowNum();
            System.out.println("获取最大的行号" + lastRowNum);
            HSSFRow sheetRow;
            for (int i = 1; i <= lastRowNum; i++) {
                sheetRow = sheet.getRow(i);
                employee employee = new employee();
                /*主键字段会自动生成不必我们自己插入数据*/
                //getCellValue(sheetRow.getCell(0));
                employee.setUsername((String) getCellValue(sheetRow.getCell(1)));
                employee.setInputtime((Date) getCellValue(sheetRow.getCell(2)));
                employee.setTel(String.valueOf( getCellValue(sheetRow.getCell(3))));
                employee.setEmail(String.valueOf(getCellValue(sheetRow.getCell(4))));
                department department = new department();
                department.setName((String) getCellValue(sheetRow.getCell(5)));
                   /*根据部门名字获取部门id*/
                Long id= employeeService.getDepartmentid((String) getCellValue(sheetRow.getCell(5)));
                department.setId(id);
                employee.setDepartment(department);
                employeeService.OnlyaddEmployee(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
            restfulResult.setData("导入失败");
        }
        return restfulResult;
    }


    /*  注意  获取Excel 中列的数据时--不同类型的值，获取列数据的方式不同*/
    private Object getCellValue(Cell cell) {

        switch (cell.getCellType()) {

            case STRING:

                return cell.getRichStringCellValue().getString();

            case NUMERIC:

                if (DateUtil.isCellDateFormatted(cell)) {

                    return cell.getDateCellValue();

                } else {

                    return cell.getNumericCellValue();

                }

            case BOOLEAN:

                return cell.getBooleanCellValue();

            case FORMULA:

                return cell.getCellFormula();

        }

        return cell;

    }


    /*Excel文件导入*/
    /*这个文件上传有错误--无法使用*/
    @RequestMapping("/ExcelImport.action")
    @ResponseBody
    public Object ExcelImport(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        RestfulResult restfulResult = new RestfulResult();
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        /*先判断HttpServletRequest 是否含有文件类型 ,若含文件，则将httpRequest 转换为MultipartHttpServletRequest类型。*/
        //  if(request instanceof MultipartHttpServletRequest) {
        MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;
        // MultipartHttpServletRequest mhr = multipartResolver.resolveMultipart(request);
        // 获取表单参数中 参数名为file的数据 ---即文件对话框选择的文件数据
        // 获取MultipartFile文件
        MultipartFile mf = mhr.getFile("file"); // 表单里的 name='file'（即表单参数中参数名为file的参数）
        if (mf != null && mf.isEmpty() == false) {
            File webroot = new File(request.getServletContext().getRealPath("/"));
            System.out.println("webroot:" + webroot);
            // 临时目录（在webroot目录下创建的临时目录）---上传文件暂时存放的地方
            File tmpdir = new File(webroot, "/static/Exceltmp");
            // 如果没有就自动生成
            tmpdir.mkdirs();
            // 得到初始文件名
            String filename = mf.getOriginalFilename();
            System.out.println(filename);
            // 得到不重复的前缀名
            /*
             * String prefix = guidUtil.gen(); System.out.println(prefix );
             */
            // 获取文件后缀名---为了等下生成唯一的临时文件名
            String suffix = getSuffix(filename);
            System.out.println("suffix:" + suffix);
            // // 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
            // 得到唯一的临时文件名--记录
            String tmpName = makeFileName(suffix);
            System.out.println("tmpName:" + tmpName);

            // 在临时目录下临时文件
            // 临时文件路径
            File tmpFile = new File(tmpdir, tmpName);

            // 把上传的文件数据，转化到临时文件路径中，从而生成临时文件
            // 接收上传 ...（转存文件到tmpFile中--转存文件到指定的文件或路径中。）
            mf.transferTo(tmpFile);
            restfulResult.setData("文件上传成功");

               /* //获取上传文件的数据源（输入流）
                FileInputStream fileInputStream = new FileInputStream(tmpFile);
                 //利用上传文件的数据源去创建HSSFWorkbook对象--即工作簿（Excel）对象
                HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);*/

            return restfulResult;
        }
        System.out.println("MultipartFile为空");
        // }
        restfulResult.setData("文件上传失败");
        return restfulResult;
    }

}