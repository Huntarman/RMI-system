package Client;

import model.Client;
import model.Status;

public class ClientStatus {
    private int id;
    private Status status;

    public ClientStatus(int id1){
        this.id = id1;
        this.status = Status.NEW;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }
}
