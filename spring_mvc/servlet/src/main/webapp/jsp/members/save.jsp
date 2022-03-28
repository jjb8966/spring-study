<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");
    String userName = request.getParameter("userName");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(userName, age);
    System.out.println("member = " + member);
    memberRepository.save(member);
%>

<html>
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
</head>
<body>
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUserName()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
