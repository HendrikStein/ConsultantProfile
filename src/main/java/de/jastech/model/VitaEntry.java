package de.jastech.model;

import de.jastech.utils.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * @author Hendrik Stein
 */
@Data
public class VitaEntry {
    private String station;
    private Date start;
    private Date end;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(DateUtils.format(start))
                .append(" - ")
                .append(DateUtils.format(end))
                .append("\r")
                .append(station);
        return builder.toString();
    }

    public Date getEnd() {
        if (end == null) {
            return new Date();
        }
        return end;
    }

}
