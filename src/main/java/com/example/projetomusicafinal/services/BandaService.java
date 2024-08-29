package com.example.projetomusicafinal.services;

import com.example.projetomusicafinal.exceptions.BandaAlreadyExistsException;
import com.example.projetomusicafinal.models.AvaliacaoBanda;
import com.example.projetomusicafinal.models.Banda;
import com.example.projetomusicafinal.repositories.AvaliacaoBandaRepository;
import com.example.projetomusicafinal.repositories.BandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandaService { //é a classe de serviço que contém a lógica de negócios para criar uma banda.

    @Autowired //cria uma instância do BandaRepository e a fornece ao BandaService quando o BandaService é criado.
    private BandaRepository bandaRepository;

    @Autowired
    private AvaliacaoBandaRepository avaliacaoBandaRepository;

    public Banda createBanda(Banda banda) { //salva a banda no banco de dados.
        Banda existingBanda = bandaRepository.findByNome(banda.getNome());
        //aqui verificamos se a banda já existe no banco de dados atraves do nome e não pelo id da banda, pois o id é gerado automaticamente quando uma nova entidade é salva no banco de dados.
        //para uma nova banda, o ID ainda não foi gerado no momento da criação, então a verificação findById(banda.getId()) provavelmente retornará null, mesmo que já exista uma banda com o mesmo nome no banco de dados.
        //O ID é gerado pelo banco de dados no momento em que a entidade é salva.
        //Se tentar buscar uma banda pelo ID antes de salvá-la no banco de dados, a busca provavelmente retornará null, porque o ID ainda não existe no banco de dados.
        //Por outro lado, o nome da banda é um atributo que você define antes de salvar a banda no banco de dados. Portanto, pode usar o nome para verificar se já existe uma banda com o mesmo nome no banco de dados antes de tentar salvar a nova banda.
        //Se usar findById(banda.getId()) para verificar a existência de uma banda antes de salvá-la, não conseguirá detectar se já existe uma banda com o mesmo nome.
        if (existingBanda != null) {
            throw new BandaAlreadyExistsException("A banda com o nome " + banda.getNome() + " já existe.");
        }
        return bandaRepository.save(banda);
    } //Quando chamamos o método save do JpaRepository, ele insere a entidade no banco de dados se ela ainda não existir,
    // ou atualiza a entidade existente se ela já existir.

    public Banda findById(Long idBanda) {
        return bandaRepository.findById(idBanda).orElse(null);
    }

    public void updateMedia(Banda banda) {
        List<AvaliacaoBanda> avaliacoes = avaliacaoBandaRepository.findAllByIdBanda(banda.getId());
        double soma = 0.0;
        for (AvaliacaoBanda avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        double media = soma / avaliacoes.size();
        banda.setMedia(media);
        bandaRepository.save(banda);
    }

    public Page<Banda> findAll(Pageable pageable) {
        return (Page<Banda>) bandaRepository.findAll(pageable);
    }
}
