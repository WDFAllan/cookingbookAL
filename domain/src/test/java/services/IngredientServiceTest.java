package services;

import com.example.dtos.IngredientDto;
import com.example.port.IngredientPort;
import com.example.services.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientPort ingredientPort;

    @InjectMocks
    private IngredientService ingredientService;

    // ── getAllIngredients ────────────────────────────────────────────────────

    @Test
    void getAllIngredients_retourneLaListeDuPort() {
        List<IngredientDto> expected = List.of(
                new IngredientDto("Farine", 200, "g"),
                new IngredientDto("Sucre", 100, "g")
        );
        when(ingredientPort.getAllIngredients()).thenReturn(expected);

        List<IngredientDto> result = ingredientService.getAllIngredients();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Farine");
        assertThat(result.get(1).getName()).isEqualTo("Sucre");
        verify(ingredientPort).getAllIngredients();
    }

    @Test
    void getAllIngredients_listeVide_retourneListeVide() {
        when(ingredientPort.getAllIngredients()).thenReturn(List.of());

        List<IngredientDto> result = ingredientService.getAllIngredients();

        assertThat(result).isEmpty();
        verify(ingredientPort).getAllIngredients();
    }

    // ── addIngredient ────────────────────────────────────────────────────────

    @Test
    void addIngredient_appellePortSavePourChaqueIngredient() {
        List<IngredientDto> ingredients = List.of(
                new IngredientDto("Farine", 200, "g"),
                new IngredientDto("Beurre", 100, "g"),
                new IngredientDto("Sel", 5, "g")
        );

        ingredientService.addIngredient(ingredients);

        // save() doit être appelé une fois par ingrédient, dans l'ordre
        verify(ingredientPort).save(ingredients.get(0));
        verify(ingredientPort).save(ingredients.get(1));
        verify(ingredientPort).save(ingredients.get(2));
        verify(ingredientPort, times(3)).save(any(IngredientDto.class));
    }

    @Test
    void addIngredient_listeVide_nAppellePasSave() {
        ingredientService.addIngredient(List.of());

        verify(ingredientPort, never()).save(any(IngredientDto.class));
    }

    @Test
    void addIngredient_unSeulIngredient_appellePortUneFois() {
        IngredientDto seul = new IngredientDto("Vanille", 1, "gousse");

        ingredientService.addIngredient(List.of(seul));

        verify(ingredientPort, times(1)).save(seul);
    }
}
