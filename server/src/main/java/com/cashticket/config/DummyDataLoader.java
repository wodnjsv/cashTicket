package com.cashticket.config;

import com.cashticket.entity.Concert;
import com.cashticket.entity.User;
import com.cashticket.repository.ConcertRepository;
import com.cashticket.repository.UserRepository;
import com.cashticket.service.AuctionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DummyDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final AuctionService auctionService;

    public DummyDataLoader(UserRepository userRepository,
                           ConcertRepository concertRepository,
                           AuctionService auctionService) {
        this.userRepository = userRepository;
        this.concertRepository = concertRepository;
        this.auctionService = auctionService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user = User.builder()
                    .userId("tester")
                    .email("test@example.com")
                    .password("password")
                    .nickname("Tester")
                    .birthDay(LocalDate.now().minusYears(25))
                    .phoneNumber("010-0000-0000")
                    .build();
            userRepository.save(user);
        }

        if (concertRepository.count() == 0) {
            Concert concert = Concert.builder()
                    .title("Dummy Concert")
                    .artist("Dummy Artist")
                    .posterImgURL("https://example.com/poster.jpg")
                    .place("Online")
                    .date(LocalDate.now().plusDays(1))
                    .dateTime(LocalDateTime.now().plusDays(1).withHour(20).withMinute(0))
                    .description("Demo concert for testing")
                    .category("TEST")
                    .isAuction(true)
                    .build();
            concertRepository.save(concert);

            auctionService.startAuction(concert.getId(), 10000,
                    concert.getDateTime().minusHours(1));
        }
    }
}
