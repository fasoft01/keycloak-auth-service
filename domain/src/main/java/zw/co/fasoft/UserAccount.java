//package zw.co.fasoft;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//
///**
// * @author Fasoft
// * @date 2/May/2024
// */
//@Data
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@EqualsAndHashCode(callSuper = true)
//public class UserAccount extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String username;
//    private String email;
//    private String phoneNumber;
//    @Enumerated(EnumType.STRING)
//    private Status status;
//    @Enumerated(EnumType.STRING)
//    private UserGroup userGroup;
//    @JsonIgnore
//    @OneToOne
//    Client client;
//    @JsonIgnore
//    @OneToOne
//    Merchant merchant;
//    @JsonIgnore
//    @OneToOne
//    Cashier cashier;
//}