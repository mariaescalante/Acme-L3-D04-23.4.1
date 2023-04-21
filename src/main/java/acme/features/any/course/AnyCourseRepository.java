
package acme.features.any.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Course;
import acme.entities.Lecture;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnyCourseRepository extends AbstractRepository {

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findManyCourses();

	@Query("select m.lecture from Membership m where m.course.id = :courseId")
	Collection<Lecture> findManyLecturesByCourseId(int courseId);
}
