package com.evans.technologies.conductor.model;

public class StatusFotos {
    public Boolean policeRecord,criminalRecod,driverLicence,soatFront,tarjetaPropFront,tarjetaPropReverse;
    public int tipoSocio;

    public Boolean getPoliceRecord() {
        return policeRecord;
    }

    public void setPoliceRecord(Boolean policeRecord) {
        this.policeRecord = policeRecord;
    }

    public Boolean getCriminalRecod() {
        return criminalRecod;
    }

    public void setCriminalRecod(Boolean criminalRecod) {
        this.criminalRecod = criminalRecod;
    }

    public Boolean getDriverLicence() {
        return driverLicence;
    }

    public void setDriverLicence(Boolean driverLicence) {
        this.driverLicence = driverLicence;
    }

    public Boolean getSoatFront() {
        return soatFront;
    }

    public void setSoatFront(Boolean soatFront) {
        this.soatFront = soatFront;
    }

    public Boolean getTarjetaPropFront() {
        return tarjetaPropFront;
    }

    public void setTarjetaPropFront(Boolean tarjetaPropFront) {
        this.tarjetaPropFront = tarjetaPropFront;
    }

    public Boolean getTarjetaPropReverse() {
        return tarjetaPropReverse;
    }

    public void setTarjetaPropReverse(Boolean tarjetaPropReverse) {
        this.tarjetaPropReverse = tarjetaPropReverse;
    }

    public int getTipoSocio() {
        return tipoSocio;
    }

    public void setTipoSocio(int tipoSocio) {
        this.tipoSocio = tipoSocio;
    }
}
