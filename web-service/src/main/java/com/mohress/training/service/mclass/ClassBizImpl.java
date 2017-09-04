package com.mohress.training.service.mclass;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mohress.training.dao.TblClassMemberDao;
import com.mohress.training.dao.TblCourseDao;
import com.mohress.training.dto.QueryDto;
import com.mohress.training.dto.mclass.ClassItemDto;
import com.mohress.training.dto.mclass.ClassRequestDto;
import com.mohress.training.entity.TblCourse;
import com.mohress.training.entity.TblTeacher;
import com.mohress.training.entity.agency.TblAgency;
import com.mohress.training.entity.mclass.TblClass;
import com.mohress.training.entity.mclass.TblClassMember;
import com.mohress.training.service.BaseManageService;
import com.mohress.training.service.ModuleBiz;
import com.mohress.training.service.agency.AgencyQuery;
import com.mohress.training.service.teacher.TeacherQuery;
import com.mohress.training.util.BusiVerify;
import com.mohress.training.util.Convert;
import com.mohress.training.util.JsonUtil;
import com.mohress.training.util.SequenceCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 班级管理
 * Created by qx.wang on 2017/8/16.
 */
@Slf4j
@Service
public class ClassBizImpl implements ModuleBiz {

    @Resource
    private TblCourseDao tblCourseDao;
    @Resource
    private TblClassMemberDao tblClassMemberDao;
    @Resource
    private BaseManageService classServiceImpl;
    @Resource
    private BaseManageService agencyServiceImpl;
    @Resource
    private BaseManageService teacherServiceImpl;

    @Override
    public void newModule(String o, String agencyId) {
        Preconditions.checkNotNull(o);
        ClassRequestDto classRequestDto = null;
        try {
            classRequestDto = JsonUtil.getInstance().convertToBean(ClassRequestDto.class, String.valueOf(o));
        } catch (Exception e) {
            log.error("新建机构反序列化失败 {}", o, e);
        }
        classServiceImpl.newModule(buildInsertClass(classRequestDto, agencyId));
    }

    @Override
    public void delete(List<String> ids) {
        Preconditions.checkArgument(!CollectionUtils.isEmpty(ids));
        classServiceImpl.delete(ids);
    }

    @Override
    public void update(String o) {
        Preconditions.checkNotNull(o);
        ClassRequestDto classRequestDto = null;
        try {
            classRequestDto = JsonUtil.getInstance().convertToBean(ClassRequestDto.class, String.valueOf(o));
        } catch (Exception e) {
            log.error("新建机构反序列化失败 {}", o, e);
        }
        classServiceImpl.update(buildUpdateClass(classRequestDto));
    }

    @Override
    public Object query(QueryDto pageDto) {
        Preconditions.checkNotNull(pageDto);
        Preconditions.checkArgument(pageDto.getPage() >= 0);
        Preconditions.checkArgument(pageDto.getPageSize() > 0);
        List<TblClass> tblClasses = classServiceImpl.query(buildClassQuery(pageDto));

        List<ClassItemDto> classItemDtos = Convert.convertClass(tblClasses);

        if (CollectionUtils.isEmpty(classItemDtos)) {
            return classItemDtos;
        }

        for (ClassItemDto dto : classItemDtos) {
            try {
                List<TblClassMember> classMembers = tblClassMemberDao.selectByClassId(dto.getClassId());
                if (CollectionUtils.isEmpty(classMembers)) {
                    continue;
                }
                String courseId = dto.getCourseId();
                TblCourse tblCourse = tblCourseDao.selectByCourseId(courseId);
                BusiVerify.verifyNotNull(tblCourse, "查询关联课程为空" + pageDto);
                TeacherQuery query = new TeacherQuery();
                query.setTeacherId(tblCourse.getTeacherId());
                List<TblTeacher> teachers = teacherServiceImpl.query(query);
                if (!CollectionUtils.isEmpty(teachers)) {
                    dto.setTeacherName(teachers.get(0).getName());
                }
                dto.setCourseName(tblCourse.getCourseName());
                AgencyQuery agencyQuery = new AgencyQuery(10, 0);
                agencyQuery.setAgencyId(dto.getAgencyId());
                List<TblAgency> agencyList = agencyServiceImpl.query(agencyQuery);
                if (!CollectionUtils.isEmpty(agencyList)) {
                    dto.setAgencyName(agencyList.get(0).getAgencyName());
                }
                List<String> studentIds = Lists.newArrayList();
                for (TblClassMember member : classMembers) {
                    studentIds.add(member.getStudentId());
                }

                dto.setStudentIds(studentIds);
            } catch (Exception e) {
                log.error("查询班级中元素失败 {}", e.getMessage());
            }
        }
        return classItemDtos;
    }

    @Override
    public void checkDelete(String agencyId, List<String> ids) {
    }

    private ClassQuery buildClassQuery(QueryDto pageDto) {
        ClassQuery classQuery = new ClassQuery(pageDto.getPageSize(), pageDto.getPage());
        classQuery.setClassname(pageDto.getClassname());
        classQuery.setAgencyId(pageDto.getAgencyId());
        classQuery.setStage(pageDto.getStage());
        return classQuery;
    }

    private ClassStudent buildInsertClass(ClassRequestDto classRequestDto, String agencyId) {
        TblClass tblClass = new TblClass();
        BeanUtils.copyProperties(classRequestDto, tblClass, "startTime", "endTime", "onClassTime", "offClassTime");
        tblClass.setStartTime(new Date(classRequestDto.getStartTime()));
        tblClass.setEndTime(new Date(classRequestDto.getEndTime()));
        tblClass.setOnClassTime(new Date(classRequestDto.getOnClassTime()));
        tblClass.setOffClassTime(new Date(classRequestDto.getOffClassTime()));
        tblClass.setClassId(SequenceCreator.getClassId());
        tblClass.setAgencyId(agencyId);

        if (classRequestDto.getStudentIds() == null) {
            return new ClassStudent(tblClass, Collections.<TblClassMember>emptyList());
        }
        List<TblClassMember> classMembers = Lists.newArrayList();
        for (String id : classRequestDto.getStudentIds()) {
            TblClassMember tblClassMember = new TblClassMember();
            tblClassMember.setClassId(tblClass.getClassId());
            tblClassMember.setStudentId(id);
            classMembers.add(tblClassMember);
        }

        return new ClassStudent(tblClass, classMembers);
    }

    private ClassStudent buildUpdateClass(ClassRequestDto classRequestDto) {
        TblClass tblClass = new TblClass();
        BeanUtils.copyProperties(classRequestDto, tblClass);
        tblClass.setStartTime(new Date(classRequestDto.getStartTime()));
        tblClass.setEndTime(new Date(classRequestDto.getEndTime()));
        tblClass.setOnClassTime(new Date(classRequestDto.getOnClassTime()));
        tblClass.setOffClassTime(new Date(classRequestDto.getOffClassTime()));

        List<TblClassMember> tblClassMembers = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(classRequestDto.getStudentIds())) {
            for (String id : classRequestDto.getStudentIds()) {
                TblClassMember member = new TblClassMember();
                member.setStudentId(id);
                member.setClassId(classRequestDto.getClassId());
                tblClassMembers.add(member);
            }
        }
        return new ClassStudent(tblClass, tblClassMembers);
    }
}
