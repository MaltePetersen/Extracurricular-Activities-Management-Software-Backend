package com.main.service;

import com.main.model.AfterSchoolCare;
import com.main.model.Attendance;
import com.main.repository.AfterSchoolCareRepository;
import com.main.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AfterSchoolCareServiceImpl implements AfterSchoolCareService {

	private AfterSchoolCareRepository repository;

	private AttendanceRepository attendanceRepository;

	public AfterSchoolCareServiceImpl(AfterSchoolCareRepository repository, AttendanceRepository attendanceRepository) {
		this.repository = repository;
		this.attendanceRepository = attendanceRepository;
	}

	@Override
	public List<AfterSchoolCare> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
	}

    @Override
    public AfterSchoolCare findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("after school care id not found: " + id));
    }

    @Override
	public AfterSchoolCare save(AfterSchoolCare newAfterSchoolCare) {
    	repository.save(newAfterSchoolCare);

		return newAfterSchoolCare;
	}

	/***
	 *
	 * Fügt einem nachmittagsbetreuungs-Objekt eine Anwesenheit hinzu und speichert es.
	 *
	 * @param id
	 * @param attendance
	 * @return gespeichertes Nachmittagsbetreuungs-Objekt
	 */
	@Override
	public AfterSchoolCare addAttendance(Long id, Attendance attendance) {

		AfterSchoolCare afterSchoolCare = this.findOne(id);
		attendanceRepository.save(attendance);
		afterSchoolCare.addAttendance(attendance);
		return this.save(afterSchoolCare);
	}


	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
