package testing.repository;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.ReservationRepository;
import de.neoventus.rest.dto.ReservationDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testing.AbstractTest;

import java.util.Date;
import java.util.List;

/**
 * testing the reservation repository methods
 *
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
 */
public class ReservationRepositoryTest extends AbstractTest {

	private ReservationRepository reservationRepository;
	private DeskRepository deskRepository;

	private Desk desk = null;

	@Autowired
	void setReservationRepository(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Autowired
	void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}

	private Desk getDesk() {
		if (desk == null) {
			desk = new Desk();
			desk.setNumber(999);

			deskRepository.save(getDesk());
		}
		return desk;
	}

	/**
	 * Test the custom implementation method for saving reservationsby dto
	 */
	@Test
	public void testSaveByDto() {

		ReservationDto dto = new ReservationDto();

		dto.setDesk(deskRepository.findByNumber(getDesk().getNumber()).getId());
		dto.setTime(new Date());

		reservationRepository.save(dto);

		List<Reservation> reservationList = (List<Reservation>) reservationRepository.findAll();

		Assert.assertNotNull(reservationList);
		Assert.assertTrue(reservationList.size() == 1);
	}


	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		reservationRepository.deleteAll();
		deskRepository.delete(desk);
	}
}
