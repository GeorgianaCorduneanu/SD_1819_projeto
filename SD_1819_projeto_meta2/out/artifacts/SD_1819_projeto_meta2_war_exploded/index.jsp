<%--
  Created by IntelliJ IDEA.
  User: ginjo
  Date: 02/12/2018
  Time: 11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!--<%@ page contentType="text/html;charset=UTF-8" language="java" %> -->
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
      <s:text name="Prime numbers needed?" />
      <s:form action="menu_inicial" method="post"> <!--o nome da action tem que ser o mesmo que no struts-->
        <s:textfield name="login_bean.verifica_login" />  <!--foi a action respetiva chamar o get que tenha primesBean a frente-->
        <s:submit /> <!-- quando se faz submit vai a action de primesaction por causa do ficheiro struts-->
      </s:form>
  </body>
</html>
