package com.dyzhome.controller;

import cn.hutool.core.util.ObjectUtil;
import com.dyzhome.entity.DataBaseEntity;
import com.dyzhome.entity.DataBaseParams;
import com.dyzhome.entity.Settings;
import com.dyzhome.service.SysGeneratorService;
import com.dyzhome.utils.JdbcUtils;
import com.dyzhome.utils.PageUtils;
import com.dyzhome.utils.Query;
import com.dyzhome.utils.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * 代码生成器
 *
 * @author Dyz
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    @ResponseBody
    @GetMapping("/connect")
    public R getConnect(@Valid DataBaseParams params) throws Exception {
        Settings.initSettings(params);
        String uuid = JdbcUtils.getConnection(new DataBaseEntity(params));
        if (ObjectUtil.isNull(JdbcUtils.connection)){
            return R.error();
        }
        return R.ok().put("connect", uuid);
    }

    @ResponseBody
    @GetMapping("/check")
    public R check(String uuid) throws Exception {
        if (Objects.isNull(JdbcUtils.uuid) || !JdbcUtils.uuid.equals(uuid)) {
            return R.error().put("success", false);
        }
        return R.ok().put("success", true);
    }

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) throws Exception{
        PageUtils pageUtil = sysGeneratorService.queryList(new Query(params));
        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, HttpServletResponse response) throws Exception {
        byte[] data = sysGeneratorService.generatorCode(tables.split(","), JdbcUtils.connection);
        String fileName = "Code" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".zip";
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
