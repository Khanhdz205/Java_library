package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.PersonalSchedule;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.entity.Subject;
import com.library.khanhnqph57627.repository.PersonalScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalScheduleService {

    @Autowired
    private PersonalScheduleRepository personalScheduleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    public List<PersonalSchedule> getAllSchedules() {
        return personalScheduleRepository.findAll();
    }

    public List<PersonalSchedule> getSchedulesByUser(Integer userId) {
        return personalScheduleRepository.findByUserId(userId);
    }

    public List<PersonalSchedule> getTodaySchedule(Integer userId, DayOfWeek dayOfWeek) {
        return personalScheduleRepository.findTodaySchedule(userId, dayOfWeek);
    }

    public List<PersonalSchedule> getActiveSchedulesByUser(Integer userId) {
        return personalScheduleRepository.findByUserIdAndIsActiveTrue(userId);
    }

    public Optional<PersonalSchedule> getScheduleById(Integer id) {
        return personalScheduleRepository.findById(id);
    }

    public PersonalSchedule saveSchedule(PersonalSchedule schedule) {
        return personalScheduleRepository.save(schedule);
    }

    public PersonalSchedule createSchedule(Integer userId, Integer subjectId, PersonalSchedule scheduleData) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Subject> subject = subjectService.getSubjectById(subjectId);

        if (user.isPresent() && subject.isPresent()) {
            scheduleData.setUser(user.get());
            scheduleData.setSubject(subject.get());
            return personalScheduleRepository.save(scheduleData);
        }
        throw new RuntimeException("User hoặc Subject không tồn tại");
    }

    public void deleteSchedule(Integer id) {
        personalScheduleRepository.deleteById(id);
    }

    public boolean toggleScheduleStatus(Integer id) {
        Optional<PersonalSchedule> schedule = personalScheduleRepository.findById(id);
        if (schedule.isPresent()) {
            PersonalSchedule ps = schedule.get();
            ps.setActive(!ps.isActive());
            personalScheduleRepository.save(ps);
            return true;
        }
        return false;
    }

    public List<Integer> getUserSubjectIds(Integer userId) {
        return personalScheduleRepository.findSubjectIdsByUserId(userId);
    }

    public boolean hasSubjectInSchedule(Integer userId, Integer subjectId) {
        return personalScheduleRepository.existsByUserIdAndSubjectId(userId, subjectId);
    }
}