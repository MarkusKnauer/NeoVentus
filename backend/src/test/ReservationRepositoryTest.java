import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.ReservationRepository;
import de.neoventus.rest.dto.ReservationDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * testing the reservation repository methods
 *
 * @author Tim Heidelbach
 * @version 0.0.1
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
	 * Test the reservation search by reservationId
	 */
	@Test
	public void testSearchById() {

		Reservation reservation1 = new Reservation();
		reservation1.setTime(new Date());
		reservation1.setDesk(getDesk());
		reservation1.setReservationId(999);
		reservationRepository.save(reservation1);

		Reservation reservation2 = reservationRepository.findByReservationId(reservation1.getReservationId());

		Assert.assertEquals(reservation1.getId(), reservation2.getId());
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

		Reservation newestReservation = reservationRepository.findFirstByOrderByReservationIdDesc();

		Assert.assertEquals(dto.getTime(), newestReservation.getTime());
	}

	/**
	 * Test if auto incremented reservationId works
	 */
	@Test
	public void testBeforeSaveEvent() {

		int reservatoinId = 999;

		Reservation reservation1 = new Reservation();
		reservation1.setReservationId(999);
		reservation1.setDesk(getDesk());
		reservation1.setTime(new Date());
		reservationRepository.save(reservation1);

		Reservation reservation2 = new Reservation();
		reservation2.setDesk(getDesk());
		reservation2.setTime(new Date());
		reservationRepository.save(reservation2);

		Assert.assertTrue(reservation2.getReservationId() == ++reservatoinId);
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
