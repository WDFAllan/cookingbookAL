package services;

import com.example.dtos.RatingDto;
import com.example.port.RatingPort;
import com.example.services.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingPort ratingPort;

    @InjectMocks
    private RatingService ratingService;

    // ── submitRating ─────────────────────────────────────────────────────────

    @Test
    void submitRating_delegueAuPortEtRetourneDto() {
        RatingDto expected = new RatingDto(4.5, 10L, 5);
        when(ratingPort.submitRating(1, 42L, 5)).thenReturn(expected);

        RatingDto result = ratingService.submitRating(1, 42L, 5);

        assertThat(result.getAverageRate()).isEqualTo(4.5);
        assertThat(result.getRatingCount()).isEqualTo(10L);
        assertThat(result.getUserRating()).isEqualTo(5);
        verify(ratingPort).submitRating(1, 42L, 5);
    }

    @Test
    void submitRating_premiereNote_moyenneEgaleALaNote() {
        RatingDto expected = new RatingDto(3.0, 1L, 3);
        when(ratingPort.submitRating(2, 7L, 3)).thenReturn(expected);

        RatingDto result = ratingService.submitRating(2, 7L, 3);

        assertThat(result.getAverageRate()).isEqualTo(3.0);
        assertThat(result.getRatingCount()).isEqualTo(1L);
    }

    // ── getRating ─────────────────────────────────────────────────────────────

    @Test
    void getRating_avecUserId_retourneDtoAvecNotePersonnelle() {
        RatingDto expected = new RatingDto(3.8, 5L, 4);
        when(ratingPort.getRating(1, 42L)).thenReturn(expected);

        RatingDto result = ratingService.getRating(1, 42L);

        assertThat(result.getUserRating()).isEqualTo(4);
        assertThat(result.getAverageRate()).isEqualTo(3.8);
        verify(ratingPort).getRating(1, 42L);
    }

    @Test
    void getRating_sansUserId_retourneDtoSansNotePersonnelle() {
        RatingDto expected = new RatingDto(3.8, 5L, null);
        when(ratingPort.getRating(1, null)).thenReturn(expected);

        RatingDto result = ratingService.getRating(1, null);

        assertThat(result.getUserRating()).isNull();
        assertThat(result.getRatingCount()).isEqualTo(5L);
        verify(ratingPort).getRating(1, null);
    }

    @Test
    void getRating_aucuneNote_retourneMoyenneZero() {
        RatingDto expected = new RatingDto(0.0, 0L, null);
        when(ratingPort.getRating(99, null)).thenReturn(expected);

        RatingDto result = ratingService.getRating(99, null);

        assertThat(result.getAverageRate()).isZero();
        assertThat(result.getRatingCount()).isZero();
    }
}
