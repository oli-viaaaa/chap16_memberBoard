<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>게시물 내용보기</title>

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
		// 삭제 체크
		function deleteCheck(url) {
			if (confirm('정말 삭제하시겠어요?')) {
				location.href = url;
			}
		}
	</script>
</head>
<body>
	<h1 style="text-align:center;">게시물 내용보기</h1>
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
				<col width="100" />
				<col width="500" />
			</colgroup>
			<tbody>
				<tr>
					<th align="center">제목</th>
					<td><c:out value="${board.title}" escapeXml="false"/></td>
				</tr>
				<tr>
					<th align="center">작성자</th>
					<td><c:out value="${board.id}" /></td>
				</tr>
				<tr>
					<th align="center">조회수</th>
					<td><c:out value="${board.hit}" /></td>
				</tr>
	
				<tr>
					<th align="center">등록 일시</th>
					<td><c:out value="${board.regdate}" /></td>
				</tr>
				<tr>
					<th align="center">내용</th>
					<td>
						<c:out value="${board.content}" escapeXml="false" />
					</td>
				</tr>
			</tbody>
		</table>
		<p class="btn_align">
			<input type="button" value="목록" onclick="goUrl('<c:url value="/boardList" />');" />
			<input type="button" value="수정" onclick="goUrl('<c:url value="/boardModify?no=${board.no}" />');" />
			<input type="button" value="삭제" onclick="deleteCheck('<c:url value="/boardDelete?no=${board.no}" />');" />
			<input type="button" value="답글쓰기" onclick="goUrl('<c:url value="/boardReplyWrite?no=${board.no}" />');" />
		</p>
	</div>	
</body>
</html>