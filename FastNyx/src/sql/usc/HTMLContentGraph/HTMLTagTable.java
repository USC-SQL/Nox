package sql.usc.HTMLContentGraph;

import java.util.HashSet;
import java.util.Set;

public class HTMLTagTable {
	Set<String> table=new HashSet<String>();
	Set<String> selftable=new HashSet<String>();
	private void init_self()
	{
		selftable.add("meta");
		selftable.add("input");
		selftable.add("img");
		selftable.add("area");
		selftable.add("base");
		selftable.add("br");
		selftable.add("col");
		selftable.add("embed");
		selftable.add("frame");
		selftable.add("hr");
		selftable.add("keygen");
		selftable.add("param");
		selftable.add("source");
		selftable.add("hr");
		selftable.add("link");



	}
	public boolean selfContain(String tagname)
	{
		return selftable.contains(tagname);
	}
	public boolean IsLegal(String tagname)
	{
		return table.contains(tagname);
	}
	public HTMLTagTable(){
		init_self();
		table.add("!--");
		table.add("!DOCTYPE");
		table.add("a");
		table.add("abbr");
		table.add("acronym");
		table.add("address");
		table.add("applet");
		table.add("area");
		table.add("article");
		table.add("aside");
		table.add("audio");
		table.add("b");
		table.add("base");
		table.add("basefont");
		table.add("bdi");
		table.add("bdo");
		table.add("big");
		table.add("blockquote");
		table.add("body");
		table.add("br");
		table.add("button");
		table.add("canvas");
		table.add("caption");
		table.add("center");
		table.add("cite");
		table.add("code");
		table.add("col");
		table.add("colgroup");
		table.add("command");
		table.add("datalist");
		table.add("dd");
		table.add("del");
		table.add("details");
		table.add("dfn");
		table.add("dialog");
		table.add("dir");
		table.add("div");
		table.add("dl");
		table.add("dt");
		table.add("em");
		table.add("embed");
		table.add("fieldset");
		table.add("figcaption");
		table.add("figure");
		table.add("font");
		table.add("footer");
		table.add("form");
		table.add("frame");
		table.add("frameset");
		table.add("h");

		table.add("h1");
		table.add("h2");
		table.add("h3");
		table.add("h4");
		table.add("h5");
		table.add("h6");
		table.add("head");
		table.add("header");
		table.add("hgroup");
		table.add("hr");
		table.add("html");
		table.add("i");
		table.add("iframe");
		table.add("img");
		table.add("input");
		table.add("ins");
		table.add("kbd");
		table.add("keygen");
		table.add("label");
		table.add("legend");
		table.add("li");
		table.add("link");
		table.add("map");
		table.add("mark");
		table.add("menu");
		table.add("meta");
		table.add("meter");
		table.add("nav");
		table.add("noframes");
		table.add("noscript");
		table.add("object");
		table.add("ol");
		table.add("optgroup");
		table.add("option");
		table.add("output");
		table.add("p");
		table.add("param");
		table.add("pre");
		table.add("progress");
		table.add("q");
		table.add("rp");
		table.add("rt");
		table.add("ruby");
		table.add("s");
		table.add("samp");
		table.add("script");
		table.add("section");
		table.add("select");
		table.add("small");
		table.add("source");
		table.add("span");
		table.add("strike");
		table.add("strong");
		table.add("style");
		table.add("sub");
		table.add("summary");
		table.add("sup");
		table.add("table");
		table.add("tbody");
		table.add("td");
		table.add("textarea");
		table.add("tfoot");
		table.add("th");
		table.add("thead");
		table.add("time");
		table.add("title");
		table.add("tr");
		table.add("track");
		table.add("tt");
		table.add("u");
		table.add("ul");
		table.add("var");
		table.add("video");
		table.add("wbr");
		table.add("!doctype");
	}
}
