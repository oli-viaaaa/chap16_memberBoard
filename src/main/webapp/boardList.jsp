<%@page import="com.javalab.vo.MemberBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>

	<script type="text/javascript">
		function goUrl(url) {
			var id = '${sessionScope.member.id}';
			if(id != null && id.length > 0){
				location.href = url;
			}else{
				alert('로그인 사용자만 글을 작성할 수 있습니다.');
				if (confirm("로그인 하시겠습니까?")){
					location.href = "${contextPath}/login";
				}
			}
		}	
	</script>
	
	<style type="text/css">
		* {
			font-size: 12px;
		}
		a {
		  text-decoration: none;	/* a tag 태그 밑줄 제거 */
		}		
		p {
			width: 600px;
			text-align: right;
		}
		table{
			width: 100%;
			border: 1px solid;
			border-collapse: collapse; */  /* 테이블 border 한줄로 나오게 */
			padding : 5px; 
		}		
		table, th, td {
		  padding: 10px;
		  text-align: left;
		}
		th, td {
		  border-bottom: 1px solid #ddd;
		}
		table thead tr th {
			background-color: #05bcac;
		}
		tr:hover {
			background-color: #dce7e6;
		}
	</style>
</head>
<body>
	<h1 style="text-align:center;">게시물 목록</h1>
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
		<table>
			<colgroup>
				<col width="50" />
				<col width="300" />
				<col width="80" />
				<col width="100" />
				<col width="70" />
				<col width="70" />
				<col width="70" />
				<col width="70" />
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>등록 일시</th>
					<th>조회수</th>
					<th>그룹번호</th>
					<th>그룹내순서</th>
					<th>들여쓰기</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${boardList.size() <= 0}">
						<tr>
							<td align="center" colspan="5">등록된 게시물이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="board" items="${boardList}" varStatus="i">
							<tr>
								<td align="center">
									<c:out value="${i.count}" />
								</td>
								<td align="left">
									<c:if test="${board.reply_indent > 0}">
										<c:forEach begin="1" end="${board.reply_indent}">
											&nbsp;&nbsp; <!-- 답변글일경우 글 제목 앞에 공백을 준다. -->
										</c:forEach>
										<img src="images/reply_icon.gif">
									</c:if>
									<a href="<c:url value='/boardView?no=${board.no}' />">
									<c:out value="${board.title}" />
									</a>
								</td>
								<td align="center">
									<c:out value="${board.id}" />
								</td>								
								<td align="center">
									<c:out value="${board.regdate}" />
								</td>
								<td align="center">
									<c:out value="${board.hit}" />
								</td>
								<td align="center">
									<c:out value="${board.reply_group}" />
								</td>
								<td align="center">
									<c:out value="${board.reply_order}" />
								</td>
								<td align="center">
									<c:out value="${board.reply_indent}" />
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
	<br>
	<input type="button" value="게시물 작성" onclick="goUrl('<c:url value="/boardWrite" />');" />
</body>
</html>