<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%=request.getContextPath()%>
</body>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript">
	var data = {};
	data['eid'] = '00386769';
	$.ajax({
		url : "http://shexi6.asiapacific.hpqcorp.net:8080/zjunion/getEmpByCriteria",
		type : 'post',
		data : '{"eid":"00386769"}',
		dataType : "json",
		contentType : "application/json",
		success : function(d) {
			$('body').append(JSON.stringify(d));
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { //#3这个error函数调试时非常有用，如果解析不正确，将会弹出错误框
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus); // paser error;
		},
	// ,'error':function(req,txtstate,err){trace('Error '+txtstate);}
	});// end ajax
</script>
</html>