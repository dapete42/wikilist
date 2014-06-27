<!DOCTYPE html>
<%@page import="java.util.Locale"%>
<%@page import="toollabs.wikilist.WikiEntry"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	final List<WikiEntry> wikiEntries = (List<WikiEntry>) request.getAttribute("wikiEntries");
	final Locale displayLocale = Locale.ENGLISH;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"
	href="//tools.wmflabs.org/style.css" />
<link rel="stylesheet" type="text/css" href="res/wikilist.css" />
<title>Wiki list</title>
<script src="//tools.wmflabs.org/admin/libs/jquery.js"></script>
<script src="//tools.wmflabs.org/admin/libs/jquery.tablesorter.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".tablesorter").tablesorter({
			sortList : [ [ 0, 0 ] ],
			widgets : [ "zebra" ],
			widgetOptions : {
				zebra : [ "even", "odd" ]
			}
		});
	});
</script>
</head>
<body>
	<div class="colmask leftmenu">
		<div class="colright">
			<div class="col1wrap">
				<div class="col1">
					<header>
						<h1>Wiki list</h1>
					</header>
					<table class="tablesorter">
						<thead>
							<tr>
								<th rowspan=2 scope=col>dbname</th>
								<th rowspan=2 scope=col>Family</th>
								<th colspan=3 scope=colgroup>Language</th>
								<th rowspan=2 scope=col>Name<br />(in wiki language)
								</th>
							</tr>
							<tr>
								<th scope=col>code</th>
								<th scope=col>in English</th>
								<th scope=col>in wiki language</th>
							</tr>
						</thead>
						<tbody>
							<%
								for (final WikiEntry entry : wikiEntries) {
									final Locale entryLocale = new Locale(entry.lang);
									String languageDisplay = entryLocale.getDisplayLanguage(displayLocale);
									String languageNative = entryLocale.getDisplayLanguage(entryLocale);
									if (languageNative.equalsIgnoreCase(languageDisplay)) {
										languageNative = "&mdash;";
									}
									if (entry.lang.equals(languageDisplay)) {
										languageDisplay = "&mdash;";
									}
							%><tr>
								<td><%=entry.dbname%></td>
								<td><%=entry.family%></td>
								<td><%=entry.lang%></td>
								<td><%=languageDisplay%></td>
								<td><%=languageNative%></td>
								<td><%=entry.name%></td>
							</tr>
							<%
								}
							%>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col2">
				<div id="logo">
					<div>
						<a href="//tools.wmflabs.org/"><img
							src="//tools.wmflabs.org/Tool_Labs_logo_thumb.png"
							alt="Wikitech and Wikimedia Labs" height="138" width="122" /></a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
