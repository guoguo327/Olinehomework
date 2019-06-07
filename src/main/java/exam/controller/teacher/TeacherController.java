package exam.controller.teacher;

import java.io.File;
import java.util.List;
import java.util.UUID;

import exam.dto.ClassDTO;
import exam.model.Question;
import exam.model.QuestionType;
import exam.model.page.PageBean;
import exam.model.role.Student;
import exam.model.role.Teacher;
import exam.service.QuestionService;
import exam.service.StudentService;
import exam.service.TeacherService;
import exam.service.impl.FileUploadServiceImpl;
import exam.util.DataUtil;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 教师部分
 *
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;
    @Resource
   	private QuestionService questionService;
    @Resource
   	private StudentService studentService;
    
    @Value("#{properties['student.pageSize']}")
    private int pageSize;
    @Value("#{properties['student.pageNumber']}")
    private int pageNumber;
   /**
    * 上传文件页面
    */
    @RequestMapping("/up")
    public String up() {
        return "teacher/upload";
    }
    
    /**
     * 上传功能
     */
  //上传 。。。。。
    @RequestMapping("/upload")
    public String up(@RequestParam(value = "excel", required = false) MultipartFile files, HttpServletRequest request) {
    	if(!files.isEmpty()) {
    	 String path = request.getSession().getServletContext().getRealPath("/upload");
	     String originalFilename = files.getOriginalFilename();	
	     originalFilename = UUID.randomUUID().toString()+originalFilename;   	      
	      File targetFile = new File(path,originalFilename );	  //在对应区域生成文件    
	      if (!targetFile.getParentFile().exists()) {
	        targetFile.getParentFile().mkdirs();
	      }else if (!targetFile.exists()) {
	        targetFile.mkdirs();
	      }	      
	      try {
		        files.transferTo(targetFile);  
		      } catch (Exception e) {
		        e.printStackTrace();
		      }	       	
    	
    	 FileUploadServiceImpl fileUp = new FileUploadServiceImpl();
	      String rootpath = path + File.separator + originalFilename;
	      System.out.println("目标文件名称："+ rootpath); 
	      List<String[]> excellist = fileUp.readExcel(rootpath);
	      
	      int len = excellist.size();  //行数
	      System.out.println("集合的长度为："+len);
	      for (int i = 0; i < len; i++) {
	        String[] fields = excellist.get(i);	        
	        String title = fields[0];	    
	        String optiona = fields[1];	     
	        String optionb = fields[2];	      
	        String optionc = fields[3];        
	        String optiond = fields[4];
	        String s= fields[5];
	        s=s.substring(0, 1);
	        Integer point = Integer.valueOf(s);	                
	        String types = fields[6];
	        String answer = fields[7];
	        if(types.equals("SINGLE")||types.equals("JUDGE")) {
	        	answer= answer.substring(0, 1);
	        }

			Question question = new Question();
			question.setType(QuestionType.valueOf(types));
			question.setAnswer(answer);
			question.setOptionA(optiona);
			question.setOptionB(optionb);
			question.setOptionC(optionc);
			question.setOptionD(optiond);
			question.setPoint(point);
			question.setTitle(title);
			question.setTeacher((Teacher) request.getSession().getAttribute("teacher"));
			questionService.saveOrUpdate(question);
	      }  
    	
    	}   	
    	return "teacher/index";		
    }	
	/**
	 * 转到教师模块主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "teacher/index";
	}

    /**
     * 转向修改密码
     */
    @RequestMapping("/password")
    public String password() {
        return "teacher/password";
    }

    /**
     * 校验旧密码
     * @param password 旧密码
     */
    @RequestMapping("/password/check")
    @ResponseBody
    public void check(String password, HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        if (teacher.getPassword().equals(password)) {
            json.addElement("result", "1");
        } else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
    
    /**
     * 获得此教师所教的班级(含专业、年级信息)
     * @param session
     * @param response
     */
    @RequestMapping("/classes")
    @ResponseBody
    public void classes(HttpSession session, HttpServletResponse response) {
    	JSONObject json = new JSONObject();
    	Teacher teacher = (Teacher) session.getAttribute("teacher");
    	List<ClassDTO> dtoes = teacherService.getClassesWithMajorAndGrade(teacher.getId());
    	JSONArray array = new JSONArray();
    	for (ClassDTO dto : dtoes) {
    		array.addObject(dto.getJSON());
    	}
    	json.addElement("result", "1").addElement("data", array);
    	DataUtil.writeJSON(json, response);
    }

    @RequestMapping("/password/modify")
    public String modifyPassword(String oldPassword, String newPassword, HttpServletRequest request, Model model) {
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        if (!checkPassword(oldPassword, newPassword, teacher)) {
            return "error";
        }
        teacherService.updatePassword(teacher.getId(), newPassword);
        teacher.setPassword(newPassword);
        teacher.setModified(true);
        model.addAttribute("message", "密码修改成功");
        model.addAttribute("url", request.getContextPath() + "/teacher/index");
        return "success";
    }

    /**
     * 检查旧密码和新密码
     * @param oldPassword 必须和session里面保存的密码一致
     * @param newPassword 必须是4-10，由数字、字母、下划线组成
     * @param teacher
     * @return 通过返回true
     */
    private boolean checkPassword(String oldPassword, String newPassword, Teacher teacher) {
        if (!teacher.getPassword().equals(oldPassword)) {
            return false;
        }
        if (!DataUtil.isValid(newPassword) || !newPassword.matches("^\\w{4,10}$")) {
            return false;
        }
        return true;
    }
    
    @RequestMapping("/stulist")
    public String stulist(String pn, String search, Model model,HttpSession session) {    	
    	Teacher teacher = (Teacher) session.getAttribute("teacher");
    	int pageCode = DataUtil.getPageCode(pn);
    	String where = " where t.id = \'"+teacher.getId()+"\'";
    	
    	if(DataUtil.isValid(search)) {
			where =where + "  s.name like '%" + search + "%'";
		}
    	
		PageBean<Student> pageBean = studentService.pageSearch2(pageCode, pageSize, pageNumber, where, null, " s.id");

		
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "teacher/student_list";
    	
    }
	
}
