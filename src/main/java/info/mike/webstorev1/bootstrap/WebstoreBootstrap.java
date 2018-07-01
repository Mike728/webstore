package info.mike.webstorev1.bootstrap;

import info.mike.webstorev1.domain.*;
import info.mike.webstorev1.repository.CategoryRepository;
import info.mike.webstorev1.repository.ProductRepository;
import info.mike.webstorev1.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class WebstoreBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    UserRepository userRepository;

    public WebstoreBootstrap(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
        initTestUser();
    }


    private void initData(){
        Category elektronika = new Category();
        elektronika.setCategoryName("Elektronika");

        Product komputer = new Product();
        komputer.setName("Komputer Infinity S700");
        komputer.setDescription("Komputer Infinity S700 został stworzony dla najbardziej wymagającej grupy " +
                "użytkowników domowych – dla graczy i osób zajmujących się obróbką wideo.");
        komputer.setManufacturer("Komputronik");
        komputer.setPrice(new BigDecimal(4999));
        komputer.setUnitsInStock(10L);
        komputer.setDiscontinued(false);

        elektronika.getProducts().add(komputer);
        komputer.getCategories().add(elektronika);

        ////////////////////////////////////////////////////////
        Category telefonyISmartwatche = new Category();
        telefonyISmartwatche.setCategoryName("Telefony i Smartwatche");


        Product lenovoK5DualSim = new Product();
        lenovoK5DualSim.setName("Lenovo K5 Dual SIM");
        lenovoK5DualSim.setDescription("Lenovo K5 Dual SIM dostępny jest w trzech stylowych kolorach.");
        lenovoK5DualSim.setManufacturer("Lenovo");
        lenovoK5DualSim.setPrice(new BigDecimal(989L));
        lenovoK5DualSim.setUnitsInStock(50L);
        lenovoK5DualSim.setDiscontinued(false);

        telefonyISmartwatche.getProducts().add(komputer);
        lenovoK5DualSim.getCategories().add(telefonyISmartwatche);

        komputer.getCategories().add(telefonyISmartwatche);
        telefonyISmartwatche.getProducts().add(komputer);

        ///////////////////////////////////////////////////////////
        Category telewizoryIRTV = new Category();
        telewizoryIRTV.setCategoryName("Telewizory i RTV");

        Product panasonicTX75EX780E = new Product();
        panasonicTX75EX780E.setName("Panasonic TX-75EX780E");
        panasonicTX75EX780E.setDescription("Funkcjonalna elegancja i wszechstronne mozliwosci połączeń to zalety to zalety, które" +
                "łączy w sobie telewizor Panasonic TX-75EX780E");
        panasonicTX75EX780E.setManufacturer("Panasonic");
        panasonicTX75EX780E.setPrice(new BigDecimal(15999));
        panasonicTX75EX780E.setUnitsInStock(30L);
        panasonicTX75EX780E.setDiscontinued(false);

        telewizoryIRTV.getProducts().add(panasonicTX75EX780E);
        panasonicTX75EX780E.getCategories().add(telewizoryIRTV);
        ////////////////////////////////////////////////////////////


        Category agd = new Category();
        agd.setCategoryName("AGD");

        Category dom = new Category();
        dom.setCategoryName("Dom");

        Category sieciikomunikacja = new Category();
        sieciikomunikacja.setCategoryName("Sieci i komunikacja");

        Category oprogramowanie = new Category();
        oprogramowanie.setCategoryName("Oprogramowanie");

        Category sprzetaudio = new Category();
        sprzetaudio.setCategoryName("Sprzęt audio");

        Category  fotoiwideo = new Category();
        fotoiwideo.setCategoryName("Foto i wideo");

        productRepository.save(komputer);
        categoryRepository.save(elektronika);
        productRepository.save(lenovoK5DualSim);
        categoryRepository.save(telefonyISmartwatche);
        productRepository.save(panasonicTX75EX780E);
        categoryRepository.save(telewizoryIRTV);

        categoryRepository.save(agd);
        categoryRepository.save(dom);
        categoryRepository.save(sieciikomunikacja);
        categoryRepository.save(oprogramowanie);
        categoryRepository.save(sprzetaudio);
        categoryRepository.save(fotoiwideo);
    }

    public void initTestUser(){
        User user = new User();
        user.setEmail("gg@wp.pl");
        user.setPassword(passwordEncoder().encode("gg"));
        user.setFirstName("Bob");
        user.setLastName("Thompson");
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));

        Address address = new Address();
        address.setCity("Sacramento");
        address.setCountry("United States");
        address.setZipcode("94239");
        address.setStreet("Neighbors Alley");
        address.setHouseNumber("17/1");

        address.setUser(user);
        user.setAddress(address);
        userRepository.save(user);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
