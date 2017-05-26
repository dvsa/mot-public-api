package uk.gov.dvsa.mot.vehicle.api ;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId ;
import java.time.ZonedDateTime ;
import java.util.Date ;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DateFormatAdapterTest
{
  private final Date theDate ;
  private final String theDateString = "2012-03-01T04:02:04.000+0000" ;
  private DateFormatAdapter adapter;

  {
    theDate = Date.from( ZonedDateTime.of( 2012, 3, 1, 4, 2, 4, 0, ZoneId.systemDefault() ).toInstant() ) ;
  }

  @Before
  public void before() {
    adapter = new DateFormatAdapter();
  }

  @Test
  public void marshal() throws Exception
  {
    assertThat(adapter.marshal( theDate ), equalTo(theDateString));
  }

  @Test
  public void unmarshal() throws Exception
  {
    assertThat(adapter.unmarshal( theDateString ), equalTo(theDate));
  }
}
