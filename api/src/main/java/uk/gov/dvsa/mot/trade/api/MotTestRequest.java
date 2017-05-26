package uk.gov.dvsa.mot.trade.api ;

import java.util.Date ;

public class MotTestRequest
{
  private Long id ;
  private Long number ;
  private Integer vehicleId ;
  private String registration ;
  private String make ;
  private Integer offset ;
  private Integer limit ;
  private Date date ;
  private Integer page ;
  private Integer pages ;
  private Integer startVehicleId ;
  private Integer endVehicleId ;

  public Long getId()
  {
    return id ;
  }

  public void setId( Long id )
  {
    this.id = id ;
  }

  public String getRegistration()
  {
    return registration ;
  }

  public void setRegistration( String registration )
  {
    this.registration = registration ;
  }

  public String getMake()
  {
    return make ;
  }

  public void setMake( String make )
  {
    this.make = make ;
  }

  public Integer getVehicleId()
  {
    return vehicleId ;
  }

  public void setVehicleId( Integer vehicleId )
  {
    this.vehicleId = vehicleId ;
  }

  public Long getNumber()
  {
    return number ;
  }

  public void setNumber( Long number )
  {
    this.number = number ;
  }

  public Integer getOffset()
  {
    return offset ;
  }

  public void setOffset( Integer offset )
  {
    this.offset = offset ;
  }

  public Integer getLimit()
  {
    return limit ;
  }

  public void setLimit( Integer limit )
  {
    this.limit = limit ;
  }

  public Date getDate()
  {
    return date ;
  }

  public void setDate( Date date )
  {
    this.date = date ;
  }

  public Integer getPages()
  {
    return pages ;
  }

  public void setPages( Integer pages )
  {
    this.pages = pages ;
  }
  
  public Integer getPage()
  {
    return page ;
  }
  
  public void setPage( Integer page )
  {
    this.page = page ;
  }

  public Integer getStartVehicleId()
  {
    return startVehicleId;
  }

  public void setStartVehicleId( Integer startVehicleId )
  {
    this.startVehicleId = startVehicleId;
  }

  public Integer getEndVehicleId()
  {
    return endVehicleId;
  }

  public void setEndVehicleId( Integer endVehicleId )
  {
    this.endVehicleId = endVehicleId;
  }
}
