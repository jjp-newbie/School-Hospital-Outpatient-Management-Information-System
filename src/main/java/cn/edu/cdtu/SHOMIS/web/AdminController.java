package cn.edu.cdtu.SHOMIS.web;


import cn.edu.cdtu.SHOMIS.model.entity.*;
import cn.edu.cdtu.SHOMIS.model.repository.AdminRepository;
import cn.edu.cdtu.SHOMIS.model.repository.DoctorRepository;
import cn.edu.cdtu.SHOMIS.model.repository.DrugRepository;
import cn.edu.cdtu.SHOMIS.model.repository.StudentRepository;
import cn.edu.cdtu.SHOMIS.service.AdminService;
import cn.edu.cdtu.SHOMIS.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author mars_sea
 */
@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private DrugRepository drugRepository;

	@GetMapping("/admin")
	public ModelAndView index(HttpServletRequest request) throws ParseException {
		AdminDO admin = (AdminDO) request.getSession().getAttribute("admin");
		ArrayList<DoctorDO> doctors = doctorService.findAllDoctor();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<Today> todays = adminService.toDay(formatter.parse(formatter.format(date)));
		ModelAndView modelAndView = new ModelAndView("/admin");
		modelAndView.addObject("doctors", doctors);
		modelAndView.addObject("todays", todays);
		return modelAndView;
	}

	@GetMapping("/statistics")
	public ModelAndView statistics(){
		ArrayList<String> statistics = adminRepository.statistics();
		ArrayList<String> data = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<>();
		for (String s: statistics) {
			String[] strings = s.split(",");
			data.add(strings[1]);
			labels.add(strings[0]);
		}

		ArrayList<String> topS = adminRepository.topStudent();
		ArrayList<TopStudent> topStudents = new ArrayList<>();
		for (String s: topS) {
			String[] strings = s.split(",");
			TopStudent student = new TopStudent(strings[0],strings[1],strings[2]);
			topStudents.add(student);
		}

		ArrayList<String> topD = adminRepository.topDoctor();
		ArrayList<String> dataD = new ArrayList<>();
		ArrayList<String> labelsD = new ArrayList<>();
		ArrayList<TopStudent> topDoctor = new ArrayList<>();
		for (String s: topD) {
			String[] strings = s.split(",");
			dataD.add(strings[2]);
			labelsD.add(strings[0]);
			TopStudent topd = new TopStudent(strings[0],strings[1],strings[2]);
			topDoctor.add(topd);
		}
		ModelAndView modelAndView = new ModelAndView("/statistics");
		modelAndView.addObject("data",data);
		modelAndView.addObject("labels",labels);
		modelAndView.addObject("datad",dataD);
		modelAndView.addObject("labelsd",labelsD);
		modelAndView.addObject("topStudents",topStudents);
		modelAndView.addObject("topDoctor",topDoctor);
		return modelAndView;
	}

	@GetMapping("/student")
	public ModelAndView sutdent() throws ParseException {
		ArrayList<StudentDO> students = (ArrayList<StudentDO>) studentRepository.findAll();
		ModelAndView modelAndView = new ModelAndView("/student");
		modelAndView.addObject("students", students);
		return modelAndView;
	}

	@PostMapping("/student")
	public @ResponseBody ModelAndView searchSutdent(String search){
		ArrayList<StudentDO> students = studentRepository.findallBySearch("%"+search+"%","%"+search+"%");
		ModelAndView modelAndView = new ModelAndView("/student");
		modelAndView.addObject("students", students);
		return modelAndView;
	}

	@PostMapping("/addstudent")
	public @ResponseBody ModelAndView addStudent(String sno, String sname, String ssex, String sage){
		StudentDO studentDO = new StudentDO(Integer.parseInt(sno),sname,Integer.parseInt(sage),ssex,"123456");
		studentRepository.save(studentDO);
		ModelAndView modelAndView = new ModelAndView("redirect:/student");
		return modelAndView;
	}

	@GetMapping("/deletestudent{sno}")
	public ModelAndView deleteStudent(@PathVariable(name="sno") String sno){
		System.out.println(sno);
		studentRepository.removeBySno(Integer.parseInt(sno));
		ModelAndView modelAndView = new ModelAndView("redirect:/student");
		return modelAndView;
	}

	@GetMapping("/doctor")
	public ModelAndView doctor() throws ParseException {
		ArrayList<DoctorDO> doctors = (ArrayList<DoctorDO>) doctorRepository.findAll();
		ModelAndView modelAndView = new ModelAndView("/doctor");
		modelAndView.addObject("doctors", doctors);
		return modelAndView;
	}

	@PostMapping("/doctor")
	public @ResponseBody ModelAndView searchDoctor(String search) {
		ArrayList<DoctorDO> doctors = doctorRepository.findAllBySearch("%"+search+"%","%"+search+"%");
		ModelAndView modelAndView = new ModelAndView("/doctor");
		modelAndView.addObject("doctors", doctors);
		return modelAndView;
	}

	@PostMapping("/adddoctor")
	public @ResponseBody ModelAndView addStudent(String dno, String dname, String dsex, String dage, String jMalibox){
		System.out.println("666666");
		DoctorDO doctorDO = new DoctorDO(Integer.parseInt(dno),dname,Integer.parseInt(dage),dsex,"123456",jMalibox);
		doctorRepository.save(doctorDO);
		ModelAndView modelAndView = new ModelAndView("redirect:/doctor");
		return modelAndView;
	}

	@GetMapping("/drug")
	public ModelAndView drug() throws ParseException {
		ArrayList<DrugDO> drugs = (ArrayList<DrugDO>) drugRepository.findAll();
		ModelAndView modelAndView = new ModelAndView("/drug");
		modelAndView.addObject("drugs", drugs);
		return modelAndView;
	}
}
