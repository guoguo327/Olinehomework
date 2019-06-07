package exam.controller.ajax;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Grade;
import exam.service.GradeService;
import exam.util.DataUtil;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;

/**
 * 年级
 *
 */
@Controller
@RequestMapping("/grade")
public class GradeController {
	
	@Resource
	private GradeService gradeService;

	/**
	 * ajax返回所年级
	 */
	@RequestMapping("/ajax")
	@ResponseBody
	public void ajax(HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		List<Grade> grades = gradeService.findAll();
		Comparator<Grade> comparator = new Comparator<Grade>() {
            public int compare(Grade s1, Grade s2) {	               
                    return s1.getGrade()-s2.getGrade();	               
            }
        };
        Collections.sort(grades,comparator);
		json.addElement("result", "1");
		for(Grade grade : grades) {
			array.addObject(grade.getJSON());
		}
		json.addElement("data", array);
		DataUtil.writeJSON(json, response);
	}
	
}
