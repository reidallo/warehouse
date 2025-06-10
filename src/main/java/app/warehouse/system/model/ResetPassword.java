package app.warehouse.system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "wr_reset_password")
@NoArgsConstructor
public class ResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "token")
    private String token;
    @OneToOne
    @JoinColumn(name = "fk_user", referencedColumnName = "id")
    private User user;
    @Column(name = "expiration_date")
    private Instant expirationDate;

    public ResetPassword(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationDate = Instant.now().plusSeconds(86400);
    }
}
