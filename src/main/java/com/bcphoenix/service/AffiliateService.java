package com.bcphoenix.service;

import com.bcphoenix.dto.AffiliateRequest;
import com.bcphoenix.model.Affiliate;
import com.bcphoenix.model.User;
import com.bcphoenix.repository.AffiliateRepository;
import com.bcphoenix.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AffiliateService {
    private final AffiliateRepository affiliateRepository;
    private final UserService userService;
    private final GameRepository gameRepository;

    public AffiliateService(AffiliateRepository affiliateRepository, UserService userService, GameRepository gameRepository){
        this.affiliateRepository=affiliateRepository;
        this.userService = userService;
        this.gameRepository = gameRepository;
    }

    public List<Affiliate> findAll(){
        return affiliateRepository.findAll();
    }
    public Affiliate findById(Integer id){
        return affiliateRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Affilié introuvable"));
    }
    public Affiliate create (AffiliateRequest request){
        String licenseNumber = request.licenseNumber().trim();
        if(affiliateRepository.existsByLicenseNumberIgnoreCase(licenseNumber)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Le numéro de licence: "+licenseNumber+" est déjà utilisé");
        }
        Affiliate affiliate = new Affiliate();
        affiliate.setActive(true);
        affiliate.setLastName(request.lastName());
        affiliate.setFirstName(request.firstName());
        affiliate.setLicenseNumber(licenseNumber);
        affiliate.setBirthDate(request.birthDate());
        User user =null;
        if(request.userId()!=null){
            if(affiliateRepository.existsByUserId(request.userId())){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Cet utilisateur est déjà lié à un affilié");
            }
             user=userService.findById(request.userId());
        }

        affiliate.setUser(user);
        return affiliateRepository.save(affiliate);
    }
    public Affiliate update(Integer id, AffiliateRequest request){
        Affiliate affiliate = findById(id);
        String licenseNumber = request.licenseNumber().trim();
        if(affiliateRepository.existsByLicenseNumberIgnoreCaseAndIdNot(licenseNumber,id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Le numéro de licence: "+licenseNumber+" est déjà utilisé");
        }
        User user =null;
        if(request.userId()!=null){
            if(affiliateRepository.existsByUserIdAndIdNot(request.userId(),id)){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Cet utilisateur est déjà lié à un affilié");
            }
            user=userService.findById(request.userId());
        }

        affiliate.setUser(user);
        affiliate.setBirthDate(request.birthDate());
        affiliate.setLicenseNumber(licenseNumber);
        affiliate.setFirstName(request.firstName());
        affiliate.setLastName(request.lastName());
        if (request.active() != null) {
            affiliate.setActive(request.active());
        }
        return affiliateRepository.save(affiliate);
    }
    public void delete(Integer id){
        Affiliate affiliate = findById(id);
        if(gameRepository.existsByAffiliateId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Des parties sont attribuées à cet affilié");
        }
        affiliateRepository.delete(affiliate);
    }

}
