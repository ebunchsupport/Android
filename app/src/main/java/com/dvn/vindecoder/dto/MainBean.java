package com.dvn.vindecoder.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IT-0870 on 27-Dec-16.
 */

public class MainBean implements Serializable {


  public String response_code;
  public String message;
  public List<RecentListResponsModel> vehicledetails;

  public MainBean(String response_code, String message, List<RecentListResponsModel> vehicledetails) {
    this.response_code = response_code;
    this.message = message;
    this.vehicledetails = vehicledetails;
  }

  public String getResponse_code() {
    return response_code;
  }

  public void setResponse_code(String response_code) {
    this.response_code = response_code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<RecentListResponsModel> getVehicledetails() {
    return vehicledetails;
  }

  public void setVehicledetails(List<RecentListResponsModel> vehicledetails) {
    this.vehicledetails = vehicledetails;
  }
}

