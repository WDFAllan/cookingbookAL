package services;

import com.example.dtos.PageResult;
import com.example.dtos.RecetteDto;
import com.example.port.RecettePort;
import com.example.services.RecetteService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecetteServiceTest {

    @Mock
    private RecettePort recettePort;

    @InjectMocks
    private RecetteService recetteService;

    // ── getAllRecette ────────────────────────────────────────────────────────

    @Test
    void getAllRecettePaged_returnsPageResult() {
        RecetteDto recette = Instancio.create(RecetteDto.class);
        PageResult<RecetteDto> expected = new PageResult<>(List.of(recette), 0, 1, 1L);
        when(recettePort.findAllPaged(0, 9, "", "date")).thenReturn(expected);

        PageResult<RecetteDto> result = recetteService.getAllRecettePaged(0, 9, "", "date");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
        verify(recettePort).findAllPaged(0, 9, "", "date");
    }

    @Test
    void getAllRecettes_returnsAllRecettes() {
        RecetteDto recette1 = Instancio.create(RecetteDto.class);
        RecetteDto recette2 = Instancio.create(RecetteDto.class);
        when(recettePort.findAll()).thenReturn(Arrays.asList(recette1, recette2));

        List<RecetteDto> result = recetteService.getAllRecette();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo(recette1.getName());
        assertThat(result.getFirst().getRate()).isEqualTo(recette1.getRate());
    }

    // ── getRecetteById ───────────────────────────────────────────────────────

    @Test
    void getRecetteById_existingId_returnsRecette() {
        RecetteDto recette = Instancio.create(RecetteDto.class);
        when(recettePort.findById(1)).thenReturn(Optional.of(recette));

        RecetteDto result = recetteService.getRecetteById(1);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(recette.getName());
    }

    @Test
    void getRecetteById_unknownId_throwsNoSuchElementException() {
        when(recettePort.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recetteService.getRecetteById(99))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");
    }

    // ── getRecetteByTag ──────────────────────────────────────────────────────

    @Test
    void getRecetteByTag_returnsMatchingRecettes() {
        RecetteDto recette1 = Instancio.create(RecetteDto.class);
        RecetteDto recette2 = Instancio.create(RecetteDto.class);
        RecetteDto recette3 = Instancio.create(RecetteDto.class);
        recette1.setTags(List.of("tag1", "tag2"));
        recette2.setTags(List.of("tag2", "tag3"));
        recette3.setTags(List.of("tag3", "tag4"));

        List<RecetteDto> recettes = List.of(recette1, recette2);

        when(recettePort.findAllByTags(recettes.getFirst().getTags()))
                .thenReturn(Collections.singletonList(recette1));

        when(recettePort.findAllByTags(List.of("tag2"))).thenReturn(
                recettes.stream()
                        .filter(r -> r.getTags().contains("tag2"))
                        .collect(Collectors.toList())
        );

        List<RecetteDto> result1 = recetteService.getRecetteByTag(List.of("tag1", "tag2"));
        List<RecetteDto> result2 = recetteService.getRecetteByTag(List.of("tag2"));

        assertThat(result1).hasSize(1);
        assertThat(result1.getFirst().getName()).isEqualTo(recette1.getName());
        assertThat(result2).hasSize(2);
        assertThat(result2).doesNotContain(recette3);
    }

    // ── addRecette ───────────────────────────────────────────────────────────

    @Test
    void addRecette_savesAndReturnsRecette() {
        RecetteDto recette = Instancio.create(RecetteDto.class);
        when(recettePort.save(Mockito.any(RecetteDto.class), isNull())).thenReturn(recette);

        RecetteDto saved = recetteService.addRecette(recette, null);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(recette.getName());
        verify(recettePort, times(1)).save(any(RecetteDto.class), isNull());
    }

    // ── updateRecette ────────────────────────────────────────────────────────

    @Test
    void updateRecette_delegatesToPort() {
        RecetteDto updated = Instancio.create(RecetteDto.class);
        when(recettePort.update(eq(1), any(RecetteDto.class), isNull())).thenReturn(updated);

        RecetteDto result = recetteService.updateRecette(1, updated, null);

        assertThat(result.getName()).isEqualTo(updated.getName());
        verify(recettePort, times(1)).update(eq(1), any(RecetteDto.class), isNull());
    }

    // ── deleteRecette ────────────────────────────────────────────────────────

    @Test
    void deleteRecette_callsPortDelete() {
        doNothing().when(recettePort).delete(eq(1), isNull());

        recetteService.deleteRecette(1, null);

        verify(recettePort, times(1)).delete(eq(1), isNull());
    }

    // ── getAllTags ───────────────────────────────────────────────────────────

    @Test
    void getAllTags_returnsSortedTags() {
        when(recettePort.getAllTags()).thenReturn(List.of("soupe", "dessert", "apéritif"));

        List<String> tags = recetteService.getAllTags();

        assertThat(tags).containsExactly("apéritif", "dessert", "soupe");
    }
}
