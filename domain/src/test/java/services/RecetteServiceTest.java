package services;

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
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RecetteServiceTest {

    @Mock
    private RecettePort recettePort;

    @InjectMocks
    private RecetteService recetteService;

    @Test
    void testGetAllRecettes() {

        //Arrange

        RecetteDto recette1 = Instancio.create(RecetteDto.class);
        RecetteDto recette2 = Instancio.create(RecetteDto.class);


        List<RecetteDto> recettes = Arrays.asList(recette1, recette2);

        when(recettePort.findAll()).thenReturn(recettes);

        //Act
        List<RecetteDto> result = recetteService.getAllRecette();

        //Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().getName()).isEqualTo(recette1.getName());
        assertThat(result.getFirst().getRate()).isEqualTo(recette1.getRate());

    }

    @Test
    void testGetRecetteByTag() {

        //Arrange
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
                    .filter(recette -> recette.getTags().contains("tag2"))
                    .collect(Collectors.toList())
        );

        //Act

        List<RecetteDto> recetteList = recetteService.getRecetteByTag(List.of("tag1", "tag2"));
        List<RecetteDto> recetteCommonTags = recetteService.getRecetteByTag(List.of("tag2"));

        //Assert

        assertThat(recetteList.size()).isEqualTo(1);
        assertThat(recetteList.getFirst().getName()).isEqualTo(recette1.getName());

        assertThat(recetteCommonTags.size()).isEqualTo(2);
        assertThat(recetteCommonTags.contains(recette3)).isFalse();


    }

    @Test
    void testAddRecette() {
        RecetteDto recette = Instancio.create(RecetteDto.class);

        when(recettePort.save(Mockito.any(RecetteDto.class))).thenReturn(recette);

        RecetteDto savedRecette = recetteService.addRecette(recette);

        assertThat(savedRecette).isNotNull();
        assertThat(savedRecette.getName()).isEqualTo(recette.getName());

    }


}
