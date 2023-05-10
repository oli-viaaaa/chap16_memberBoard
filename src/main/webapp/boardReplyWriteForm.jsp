<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta content="text/html; charset=UTF-8"></meta>
<title>답글작성</title>

	<script type="text/javascript"
		src="<c:url value="/ckeditor/ckeditor.js" />">
	</script>
	
	<style type="text/css">
		* {
			font-size: 12px;
		}
		table{
			width: 100%;
			border: 1px solid;
			border-collapse: collapse; */  /* 테이블 border 한줄로 나오게 */
			padding : 5px; 
			line-height: 20px;
		}	
		.btn_align {
			width: 100%;
			text-align: center;
		}
		table, th, td {
		  padding: 10px;
		  text-align: left;
		}
		th, td {
		  border-bottom: 1px solid #ddd;
		}
			
		table tbody tr th {
			background-color: #05bcac;
		}
	</style>

	<script type="text/javascript">
		function goUrl(url) {
			location.href = url;
		}
		// 수정 폼 체크
		function boardReplyWriteCheck() {
			var form = document.replyWriteForm;
			if (form.title.value == '') {
				alert('제목을 입력하세요.');
				form.title.focus();
				return false;
			}
			if (form.writer.value == '') {
				alert('작성자를 입력하세요');
				form.writer.focus();
				return false;
			}
			return true;
		}
	</script>
</head>
<body>
	<h1 style="text-align:center;">게시물 답변 작성</h1>
	<div style="text-align: right;">
		<c:if test="${not empty sessionScope.member.id}" >
			<strong>${sessionScope.member.name}(${sessionScope.member.id})님</strong>
			<a href="${pageContext.request.contextPath}/logout"><strong>로그아웃</strong></a>
		</c:if>
		<c:if test="${empty sessionScope.member.id}" >
			<a href="${pageContext.request.contextPath}/login"><strong>로그인</strong></a>
		</c:if>
	</div>
	<div>
		<form name="replyWriteForm"
			action="<c:url value="/boardReplyWrite" />" method="post"
			onsubmit="return boardReplyWriteCheck();">
			
			<input type="hidden" name="no" value="<c:out value="${board.no}" />" />
			<input type="hidden" name="reply_group" value="<c:out value="${board.reply_group}" />" />
			<input type="hidden" name="reply_order" value="<c:out value="${board.reply_order}" />" />
			<input type="hidden" name="reply_indent" value="<c:out value="${board.reply_indent}" />" />
			<div>				
				<table border="1" summary="답변글">
					<caption>답변글</caption>
					<colgroup>
						<col width="100" />
						<col width="500" />
					</colgroup>
					<tbody>
						<tr>
							<th align="center">원글 제목</th>
							<td>${board.title}</td>
						</tr>
						<tr>
							<th align="center">원글 작성자</th>
							<td>${board.id}</td>
						</tr>
						<tr>
							<th align="center">원글 내용</th>
							<td>
								<textarea style="width:100%" cols="80" rows="5" readonly>${board.content}
								</textarea> 
							</td>
						</tr>
					
						<tr>
							<th align="center">답글 제목</th>
							<td><input type="text" name="title" size="80" maxlength="100" /></td>
						</tr>
						<tr>
							<th align="center">답글 작성자</th>
							<td><input type="text" name="id" maxlength="20" readonly 
								value="<c:out value="${member.id}" />" /></td>
						</tr>
						<tr>
							<th align="center">답글 내용</th>
							<td>
								<textarea name="content" cols="80" rows="10">
									<c:out value="" escapeXml="false" />
								</textarea> 
								<script>CKEDITOR.replace('content');</script>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
			</div>
			<p class="btn_align">
				<input type="submit" value="저장" />
				<input type="button" value="목록" onclick="goUrl('<c:url value="/boardList" />');" />
			</p>
		</form>
	</div>	
</body>
</html>