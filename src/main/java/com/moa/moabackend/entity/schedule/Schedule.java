package com.moa.moabackend.entity.schedule;

import com.moa.moabackend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private LocalDate meetingDate;

    @Column
    private String title;

    @Column
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime meetingTime;

    @Column
    private String location;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany
//    private List<String> userNameList;

    public void update(ScheduleRequestDto requestDto) {
        this.meetingDate = LocalDate.parse(requestDto.getMeetingDate());
        this.title = requestDto.getTitle();
        this.meetingTime = LocalTime.parse(requestDto.getMeetingTime());
        this.location = requestDto.getLocation();
        this.content = requestDto.getContent();
    }
}