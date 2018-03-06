package com.apec.framework.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apec.framework.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apec.framework.base.BaseController;
import com.apec.framework.base.IJsonService;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.excel.CodeDealUtils;
import com.apec.framework.common.excel.XlsVO;
import com.apec.framework.common.exception.DispatchException;
import com.apec.framework.common.util.FileUtil;
import com.apec.framework.common.util.BaseJsonUtil;


/**
 * 类 编 号：
 * 类 名 称：XLSController 
 * 内容摘要：PDF、excel 导出
 * 请求信息 
 * 创建日期：2017/2/6 
 * 编码作者：
 * @author xx
 */
@RestController
public class XLSController extends BaseController {

	@Autowired
	private IJsonService dispatchJSONService;
	   
	@RequestMapping(value = "/{serverName}/{methodName}.xls")
	public void dispatchRequest(@PathVariable("serverName") String serverName,
			@PathVariable("methodName") String methodName, HttpServletRequest request,HttpServletResponse res) throws IOException {
		String ret = sendRequest(request, serverName, methodName);
		if(StringUtils.isEmpty(ret)) {
			return;
		}
		ResultData resultData = BaseJsonUtil.parseObject(ret, ResultData.class);
		if(!resultData.isSucceed() || null == resultData.getData()) {
			return;
		}
		XlsVO object = BaseJsonUtil.parseObject(resultData.getData().toString(), XlsVO.class);
		if(null == object) {
			return;
		}
		String url = object.getUrl();
		String fileName = CodeDealUtils.encodeFilename(object.getFileName(), request);
		FileUtil.downloadFile(res, fileName, url);
	}

	/**
	 * 请求分发
	 * @param serverName 服务名称
	 * @param methodName 调用方法名
	 * @param fileName 文件名称
	 * @param request 请求信息
	 * @return 请求返回结果
	 */
	@RequestMapping(value = "/{serverName}/{fileName}/{methodName}.xls")
	public ResponseEntity<byte[]> dispatchRequest(@PathVariable("serverName") String serverName,
												  @PathVariable("fileName") String fileName, @PathVariable("methodName") String methodName,
												  HttpServletRequest request, HttpServletResponse res) throws IOException {

		String ret = sendRequest( request, serverName, fileName + Constants.SINGLE_SLASH + methodName );
		if(StringUtils.isEmpty(ret)) {
			return   new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ResultData resultData = BaseJsonUtil.parseObject(ret, ResultData.class);
		if(!resultData.isSucceed() || null == resultData.getData()) {
			return   new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		XlsVO object = BaseJsonUtil.parseObject(resultData.getData().toString(), XlsVO.class);
		if(null == object) {
			return   new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String url = object.getUrl();
		String excelFileName = CodeDealUtils.encodeFilename(object.getFileName(), request);

		Path path = Paths.get(url);
		// 文件的存放路径
		byte[] contents  = Files.readAllBytes(path);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
		headers.setContentDispositionFormData(excelFileName, excelFileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return  new ResponseEntity<>(contents,headers, HttpStatus.OK);
	}

	/**
     * 发送请求
     * @param request 请求
     * @param methodName 调用方法名
     * @param serverName 服务名称
     * @return 请求返回结果
     */
    private String sendRequest(HttpServletRequest request, String serverName, String methodName)
    {
        String ret;
        try
        {
            ret = dispatchJSONService.service( serverName, methodName, request );
        }
        catch (DispatchException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }
        return ret;

    }
    
}