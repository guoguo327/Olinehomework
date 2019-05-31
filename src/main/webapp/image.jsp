<%@page import="java.io.OutputStream"%>
<%@ page contentType="image/jpeg" import="java.awt.*, java.awt.image.*,java.util.*,javax.imageio.*"  pageEncoding="UTF-8"%> 
<%! 
	/*
	声明,定义方法和属性以及全局变量
	*/
    Color getRandColor(int fc,int bc)   //给定范围获得随机颜色,0-255,fc到bc之间
    { 
        Random random = new Random(); 
        if(fc>255) fc=255; 
        if(bc>255) bc=255; 
        int r=fc+random.nextInt(bc-fc); 
        int g=fc+random.nextInt(bc-fc); 
        int b=fc+random.nextInt(bc-fc); 
        return new Color(r,g,b); 
    } 
%> 
<% 
    //脚本片段，定义局部变量或者调用方法，但不能定义方法  
    
	
	int codecount = 4;  //验证码数
	int linecount=7;   //干扰线			
    int width=60;       //在内存中创建图像
    int height=20; 
    
    char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z'};

	//设置页面不缓存
    response.setHeader("Pragma","No-cache");         //(HTTP1.0)
    response.setHeader("Cache-Control","no-cache");    //(HTTP1.1)
    response.setDateHeader("Expires", 0); // Expires过时期限值，GMT格式，指浏览器或缓存服务器在该时间点后必须从真正的服务器中获取新的页面信息
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
    //获取图形上下文
    Graphics g = image.getGraphics();
    
    Random random = new Random(); 
   //设定背景色
    g.setColor(Color.white);
    g.fillRect(0, 0, width, height); 
    //设置颜色,画边框
    g.setColor(Color.black);
    g.drawRect(0,0,width-1,height-1);
   //设定字体
    g.setFont(new Font("Times New Roman",Font.PLAIN,18)); 
    g.setColor(getRandColor(160,200)); 
   // 随机产生7条干扰线，使图象中的认证码不易被其它程序探测到
   g.setColor(Color.blue);
    for (int i=0;i<linecount;i++) {
        int x = random.nextInt(width); 
        int y = random.nextInt(height); 
        int xl = random.nextInt(width);  
        int yl = random.nextInt(height);  
        g.drawLine(x,y,xl,yl); 
    } 
    g.setFont(new Font("Times New Roman",Font.BOLD,18));
    String sRand=""; 
    for (int i=0;i<codecount;i++){ 
        String rand=String.valueOf(codeSequence[random.nextInt(codeSequence.length)]); 
        sRand+=rand; 
        g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110))); 
        g.drawString(rand,13*i+6,16); 
    } 
    //设置进入session属性，为在DataUtil中校验用
    session.setAttribute("rand",sRand); 
    g.dispose(); 
    OutputStream os = response.getOutputStream();
    ImageIO.write(image, "JPEG", os); 
    
    //解决非法状态exception
    os.flush();  
    os.close();  
    os=null;  
    response.flushBuffer();  
    out.clear();  
    out = pageContext.pushBody();  
    }  
    catch(IllegalStateException e)  
    {  
    System.out.println(e.getMessage());  
    e.printStackTrace();  
%>