package com.attendance.backend.service;

import com.attendance.backend.entity.Group;
import com.attendance.backend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> getAllActiveGroups() {
        return groupRepository.findAllActiveOrderByName();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<Group> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return groupRepository.existsByName(name);
    }

    public int countStudentsInGroup(Group group) {
        return groupRepository.countActiveStudentsByGroup(group);
    }
}