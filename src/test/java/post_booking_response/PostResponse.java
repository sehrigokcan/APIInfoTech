package post_booking_response;

public class PostResponse {
	private Booking booking;
	private int bookingid;

	public void setBooking(Booking booking){
		this.booking = booking;
	}

	public Booking getBooking(){
		return booking;
	}

	public void setBookingid(int bookingid){
		this.bookingid = bookingid;
	}

	public int getBookingid(){
		return bookingid;
	}
}
