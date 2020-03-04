package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "serverentry")
public class ServerEntry implements Serializable {

    public ServerEntry() {

    }

    public ServerEntry(int identifier, String campaign, LocalDateTime entryDate, long id, LocalDateTime exitDate,
                       int pageViews, boolean conversion) {
        this.identifier = identifier;
        this.campaign = campaign;
        this.entryDate = entryDate;
        this.id = id;
        this.exitDate = exitDate;
        this.pageViews = pageViews;
        this.conversion = conversion;
    }

    @Id
    @Column
    private int identifier;

    @Column
    private String campaign;

    @Column
    private LocalDateTime entryDate;

    @Column
    private long id;

    @Column
    private LocalDateTime exitDate;

    @Column
    private int pageViews;

    @Column
    private boolean conversion;

    public int getIdentifier() {
        return identifier;
    }

    public String getCampaign() { return campaign; }

    public LocalDateTime getEntryDate() { return entryDate; }

    public long getId() {
        return id;
    }

    public LocalDateTime getExitDate() { return exitDate; }

    public int getPageViews() { return pageViews; }

    public boolean getConversion() { return conversion; }

}
