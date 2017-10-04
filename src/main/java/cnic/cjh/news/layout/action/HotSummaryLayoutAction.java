package cnic.cjh.news.layout.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import cnic.cjh.spider.weibo.bean.BriefNews;
import cnic.cjh.utils.mybatis.SqlSessionSupport;

/**
 * http://47.92.29.85/HappyEraBook/book/category?book_id=xxx
 * 通过book_id返回书籍目录信心的json格式字符串 Json指定如下格式 { dataSource: [ { text:'BookName',
 * children:[ { text: 'North America', children: [ { text: 'USA', children: [ {
 * text: 'California' }, { text: 'Miami' } ] }, { text: 'Canada' }, { text:
 * 'Mexico' } ] }, { text: 'Europe', children: [ { text: 'France' }, { text:
 * 'Spain' }, { text: 'Italy' } ] }, { text: 'South America', children: [ {
 * text: 'Brazil' }, { text: 'Argentina' }, { text: 'Columbia' } ] } ] } ],
 * width: $(window).width()*0.25 - 20, uiLibrary: 'bootstrap' }
 * 
 * @author caojunhui
 *
 */
@SuppressWarnings("serial")
public class HotSummaryLayoutAction extends ActionSupport
{
	private static final Logger l = LoggerFactory.getLogger(HotSummaryLayoutAction.class);
	private InputStream htmlStream;

	/*
	 * 容器自动获取httpServletRequest
	 */
	/*
	 * 发送url请求，根据书籍ID获取 book输出流内容
	 */
	@Override
	public String execute() throws Exception
	{
		setHtmlStream();
		return super.execute();
	}

	private void setHtmlStream()
	{
		SqlSession sqlSession = SqlSessionSupport.getSqlSession();
		List<BriefNews> news_list = sqlSession.selectList(BriefNews.class.getName() + ".query24h");
		StringBuilder b = new StringBuilder(BriefNews.HEAD_HTML);
		for(BriefNews news : news_list)
		{
			b.append(news.toHtmlString());
		}
		b.append(BriefNews.FOOT_HTML);
		try
		{
			htmlStream = new ByteArrayInputStream(b.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
			l.error("不支持的字符编码格式",e);
		}
	}

	public InputStream getHtmlStream()
	{
		return htmlStream;
	}

	public static void main(String[] args)
	{
		//WebApplicationContextUtils.getWebApplicationContext
	}
}

