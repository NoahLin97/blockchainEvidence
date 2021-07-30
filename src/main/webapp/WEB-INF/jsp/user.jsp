<%--
  Created by IntelliJ IDEA.
  User: Noah
  Date: 2021/7/29
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table align='center' border='1' cellspacing='0'>
    <tr>
        <td>id</td>
        <td>userName</td>
        <td>passWord</td>
        <td>eMail</td>
    </tr>
    <c:forEach items="${user}" var="u" varStatus="st">
        <tr>
            <td>${u.id}</td>
            <td>${u.userName}</td>
            <td>${u.passWord}</td>
            <td>${u.eMail}</td>
        </tr>
    </c:forEach>
</table>
