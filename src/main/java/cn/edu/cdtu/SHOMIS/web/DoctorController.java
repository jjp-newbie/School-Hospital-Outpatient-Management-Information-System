package cn.edu.cdtu.SHOMIS.web;

import cn.edu.cdtu.SHOMIS.model.entity.RegDo;
import cn.edu.cdtu.SHOMIS.model.entity.RegisteredDO;
import cn.edu.cdtu.SHOMIS.model.entity.StudentDO;
import cn.edu.cdtu.SHOMIS.service.impl.RegisteredServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DoctorController {
    @Autowired
    protected RegisteredServiceImpl registeredService;

    @RequestMapping("/doctor")
    public ModelAndView doctorView(){
        ModelAndView doctor = new ModelAndView("doctor/doctorView");

        return doctor;
    }

    @RequestMapping("/registration")
    public ModelAndView registrationView(String sno){
        ModelAndView registration = new ModelAndView("doctor/registrationView");
        System.out.println("sno的值"+sno);
        List<RegisteredDO> registeredDOList = new ArrayList<>();
        if (sno != null && !sno.equals("")){
            int s = Integer.parseInt(sno);
            registeredDOList = registeredService.findAllByStudentSno(s);
            if (registeredDOList.size()==0){
                registration.addObject("msg", "错误！该挂号信息不存在！");
            }
        }else {
            registeredDOList = registeredService.findBystudents(2020001);
        }


        List<RegDo> regDoList = new ArrayList<>();
//        System.out.println(byDoctor_dnoOrderByrOrderDesc.size());
       for (int i = 0 ; i < registeredDOList.size(); i++){
           RegisteredDO registeredDO = registeredDOList.get(i);
           StudentDO student = registeredDO.getStudent();
           RegDo regDo = new RegDo();
           regDo.setSequence(registeredDO.getSequence());
           regDo.setSname(student.getSname());
           regDo.setSno(student.getSno());
            regDoList.add(regDo);
       }
        registration.addObject("regList", regDoList);

        return registration;
    }


}
