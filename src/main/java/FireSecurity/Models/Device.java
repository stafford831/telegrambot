package FireSecurity.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "devices")
/**
 * модель УС
 */
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="device_serial")
    private String deviceSerial;

    @Column(name="expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return deviceSerial.equals(device.deviceSerial) && expirationDate.equals(device.expirationDate) && user.equals(device.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceSerial, expirationDate, user);
    }
}
