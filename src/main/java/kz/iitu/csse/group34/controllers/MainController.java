package kz.iitu.csse.group34.controllers;

import kz.iitu.csse.group34.entities.*;
import kz.iitu.csse.group34.repositories.*;
import kz.iitu.csse.group34.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;


    @Autowired
    private ItemsRepository itemsRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping(value = "/")
    public String index(ModelMap model, @RequestParam(name = "page", defaultValue = "1") int page){
        int pageSize = 8;

        if(page<1){
            page = 1;
        }

        int totalItems = itemsRepository.countAllByDeletedAtNull();
        int tabSize = (totalItems+pageSize-1)/pageSize;
        Pageable pageable = PageRequest.of(page-1, pageSize);
        model.addAttribute("tabSize", tabSize);
        List<Items> items = itemsRepository.findAllByDeletedAtNull(pageable);
        model.addAttribute("itemler", items);
        List<Categories> categories = categoriesRepository.findAllByIdGreaterThan(0L);
        model.addAttribute("categorialar", categories);
        return "index";
    }


    @GetMapping(value = "/addItem")
    public String addItem(ModelMap model, @RequestParam(name = "page", defaultValue = "1") int page){
        List<Categories> categories = categoriesRepository.findAllByIdGreaterThan(0L);
        model.addAttribute("categorialar", categories);
        return "additem";
    }



    @PostMapping(value = "/addItem")
    public String addItem2(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "price") double price,
            @RequestParam(name = "characteristics") String characteristics,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "file") MultipartFile file,
            HttpSession session) throws IOException {


        byte[] f = new byte[0];
        try {
            f = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }
        String uuidfile = UUID.randomUUID().toString();
        String resultFilename = uuidfile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFilename));



        Categories c=categoriesRepository.getOne(id);
        if(name!="" && price!=0 && c != null && characteristics != ""){
            Users users = getUserData();
            Items items = new Items(name,price,characteristics,c,users,f,resultFilename);
            items.setCreatedAt(new Date());
            itemService.addItem(items);
            return "redirect:/";
        }
        else{
            return "redirect:/addItem";
        }
    }




    @GetMapping(path = "/register")
    public String registrPage(Model model){
        return "registration";
    }
    @PostMapping(value = "/register")
    public String add2(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "password2") String password2,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "surname") String surname,
            HttpSession session) {
        if (password.equals(password2) && (!email.equals("")) && (!password.equals(""))
                && (!password2.equals("")) && (!name.equals("")) && (!surname.equals("")) && password.length()>=6) {
            Roles role = rolesRepository.getOne(1L);
            Set<Roles> roles = new HashSet<>();
            roles.add(role);
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            String newpass = b.encode(password);
            Users users = new Users(email, name, surname,true,newpass,roles);
            users.setCreatedAt(new Date());
            userRepository.save(users);
            return "redirect:/login";
        }
        else{
            return "redirect:/register";
        }
    }


    @GetMapping(path = "/adduser")
    public String addus(Model model){

        return "adduser";

    }

    @PostMapping(value = "/adduser")
    public String adduse(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "password2") String password2,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "surname") String surname,
            @RequestParam(name = "urole",defaultValue = "false") Boolean u,
            @RequestParam(name = "mrole",defaultValue = "false") Boolean m,
            HttpSession session) {
        Set<Roles> roles = new HashSet<>();
        if(m && !u){
            Roles role = rolesRepository.getOne(3L);
            roles.add(role);
        }
        else if(u && !m){
            Roles role = rolesRepository.getOne(1L);
            roles.add(role);
        }
        else if(u && m){
            Roles role = rolesRepository.getOne(1L);
            Roles role2 = rolesRepository.getOne(3L);
            roles.add(role);
            roles.add(role2);
        }

        boolean users2=userRepository.existsByEmail(email);
        if (password.equals(password2) && (!email.equals("")) && (!password.equals(""))
                && (!password2.equals("")) && (!name.equals("")) && (!surname.equals("")) && password.length()>=6 &&  !users2) {
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            String newpass = b.encode(password);
            Users users = new Users(email,name, surname,true,newpass,roles);
            users.setCreatedAt(new Date());
            userRepository.save(users);
            return "redirect:/";
        }
        else{
            return "redirect:/adduser";
        }
    }

    @GetMapping(path = "/login")
    public String loginPage(Model model){

        return "login";

    }


    @GetMapping(path = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){

        model.addAttribute("user", getUserData());
        return "profile";

    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String usersPage(Model model){

        model.addAttribute("user", getUserData());
        Roles role = rolesRepository.getOne(2L);
        List<Users> users = userRepository.findAllByRolesNotLike(role);
        model.addAttribute("userList", users);

        return "users";

    }

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userRepository.findByEmail(secUser.getUsername());
        }
        return userData;
    }

    @GetMapping(path = "/updatedata")
    public String updatedat(Model model){
        model.addAttribute("user", getUserData());
        return "updatedata";

    }

    @GetMapping(path = "/updatepassword")
    public String updatedat1(Model model){
        model.addAttribute("user",getUserData());
        return "updatepassword";
    }


    @PostMapping(value = "/updatepassword")
    public String updatepasswordqqwe(
            @RequestParam(name = "old") String old,
            @RequestParam(name = "new") String ne,
            @RequestParam(name = "newpass") String newpass,
            HttpSession session) {

        BCryptPasswordEncoder b2 = new BCryptPasswordEncoder();
        boolean result = b2.matches(old, getUserData().getPassword());
        if ((!ne.equals("")) && (!newpass.equals("")) && result  && ne.equals(newpass) && ne.length()>=6) {
            BCryptPasswordEncoder b = new BCryptPasswordEncoder();
            String newpass2 = b.encode(ne);
            Users users=getUserData();
            users.setPassword(newpass2);
            users.setUpdatedAt(new Date());
            userRepository.save(users);
            return "redirect:/";
        }
        else{
            return "redirect:/updatepassword";
        }
    }

    @PostMapping(value = "/updatedata")
    public String adduse(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "surname") String surname,
            HttpSession session) {
        if ((!name.equals("")) && (!surname.equals(""))) {
            Users user=getUserData();
            user.setName(name);
            user.setSurname(surname);
            user.setUpdatedAt(new Date());
            userRepository.save(user);
            return "redirect:/";
        }
        else{
            return "redirect:/updatedata";
        }
    }

    @GetMapping(value = "/userbl/{id}")
    public String asdzxc3(@PathVariable(name = "id") Long id){
        Users users=userRepository.getOne(id);
        users.setIsActive(false);
        users.setUpdatedAt(new Date());
        userRepository.save(users);
        return "redirect:/";
    }

    @GetMapping(value = "/userdel/{id}")
    public String asdzxc(@PathVariable(name = "id") Long id){
        Users users=userRepository.getOne(id);
        userRepository.delete(users);
        return "redirect:/";
    }

    @GetMapping(path = "/readMore/{id}")
    public String readmore2(ModelMap model, @PathVariable(name = "id") Long id){

        Items item = itemsRepository.getOne(id);
        List<Comments>comments=commentsRepository.findAll();
        List<Comments>comments1=new ArrayList();
        for(Comments t:comments){
            if(t.getItems().getId().equals(item.getId())){
                comments1.add(t);
            }
        }
        model.addAttribute("commenty",comments1);
        if(item.getUpdatedAt()!=null){
            model.addAttribute("time",item.getUpdatedAt());
        }
        else{
            model.addAttribute("time",item.getCreatedAt());
        }
        model.addAttribute("item", item);
        return "readitem";

    }



    @GetMapping(path = "/editItem/{id}")
    public String editItem(ModelMap model, @PathVariable(name = "id") Long id,
                      HttpSession session) {
        Items item = itemsRepository.getOne(id);
        Users user = getUserData();
        if (item.getAuthor().getId().equals(user.getId())) {
            session.setAttribute("postid", id);
            model.addAttribute("item", item);
            List<Categories> categories = categoriesRepository.findAllByIdGreaterThan(0L);
            model.addAttribute("categorialar", categories);
            return "editiitem";
        }
        else{
            return "redirect:/";
        }
    }

    @PostMapping(value = "/editItem")
    public String avc(
            @RequestParam(name = "name")String name,
            @RequestParam(name = "characteristics") String characteristics,
            @RequestParam(name = "price") double price,
            @RequestParam(name = "id") Long id2,
            @RequestParam(name = "file") MultipartFile file,
            HttpSession session
    )throws IOException{

        byte[] f = new byte[0];
        try {
            f = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }
        String uuidfile = UUID.randomUUID().toString();
        String resultFilename = uuidfile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFilename));


        Categories c=categoriesRepository.getOne(id2);
        Long id=(Long)session.getAttribute("postid");
        Items i=itemsRepository.getOne(id);
        i.setCategories(c);
        i.setName(name);
        i.setCharacteristics(characteristics);
        i.setPrice(price);
        i.setUpdatedAt(new Date());
        i.setImage(f);
        i.setFile(resultFilename);
        itemsRepository.save(i);
        return "redirect:/";
    }


    @GetMapping(path = "/deleteItem/{id}")
    public String deleteItem(ModelMap model, @PathVariable(name = "id") Long id,
                         HttpSession session) {
        Items item = itemsRepository.getOne(id);
        Users user = getUserData();
        if (item.getAuthor().getId().equals(user.getId())) {
            item.setDeletedAt(new Date());
            itemsRepository.save(item);
            return "redirect:/";
        }
        else{
            return "redirect:/";
        }
    }

    @PostMapping(value = "/addcomment")
    public String avc(
            @RequestParam(name = "comment") String content,
            @RequestParam(name = "id") Long id,
            HttpSession session
    ){
        Items items=itemsRepository.getOne(id);
        Users users = getUserData();
        if(users!=null) {
            if (content != "") {
                Comments comments = new Comments(users, items, content);
                comments.setCreatedAt(new Date());
                commentsRepository.save(comments);
                return "redirect:/";
            }
            else{
                return "redirect:/";
            }
        }
        else{
            return "redirect:/login";
        }
    }


    @GetMapping(path = "/removecomm/{id}")
    public String qwe(ModelMap model, @PathVariable(name = "id") Long id,
                      HttpSession session) {
        Comments comments = commentsRepository.getOne(id);
        Users users = getUserData();
        if(users!=null) {
            if (comments.getAuthor().getId().equals(users.getId())) {
                commentsRepository.deleteById(id);
                return "redirect:/";
            } else {
                return "redirect:/";
            }
        }
        else {
            return "redirect:/login";
        }
    }

    @GetMapping(path = "/removecomm2/{id}")
    public String qwe3(ModelMap model, @PathVariable(name = "id") Long id,
                       HttpSession session) {
        Comments comments = commentsRepository.getOne(id);
        Users users = getUserData();
        commentsRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping(path = "/editcomm/{id}")
    public String qwe2(ModelMap model, @PathVariable(name = "id") Long id,
                       HttpSession session) {
        Comments comments = commentsRepository.getOne(id);
        Users users = getUserData();
        if(users!=null) {
            if (comments.getAuthor().getId().equals(users.getId())) {
                session.setAttribute("come",comments);
                model.addAttribute("come",comments);
                return "editcomment";
            } else {
                return "redirect:/";
            }
        }
        else {
            return "redirect:/login";
        }
    }

    @PostMapping(path = "/editcomment")
    public String vcd(ModelMap model,@RequestParam(name="content")String content,
                      HttpSession session) {
        Comments comments= (Comments) session.getAttribute("come");
        comments.setComment(content);
        comments.setUpdatedAt(new Date());
        commentsRepository.save(comments);
        return "redirect:/";
    }



    @GetMapping(path = "/mybaskets")
    public String mybas(ModelMap model,HttpSession session) {
        Users u=getUserData();
        if(u!=null){
            List<Basket> baskets=basketRepository.findAllByDeletedAtNullOrderByCreatedAtDesc();
            List<Basket>newbaskets=new ArrayList();
            for(Basket b:baskets){
                if(b.getAuthor().getId().equals(u.getId())){
                    newbaskets.add(b);
                }
            }

            int amount = 0;
            double total = 0;
            for(Basket b:newbaskets){
                total += b.getItems().getPrice();
                amount++;
            }

            session.setAttribute("Price",total);
            session.setAttribute("Card",newbaskets);

            model.addAttribute("basketsTotalPrice",total);
            model.addAttribute("basketsAmount",amount);
            model.addAttribute("baskets",newbaskets);
            return "mybaskets";
        }
        else{
            return "redirect:/";
        }
    }

    @GetMapping(path = "/addBasket/{id}")
    public String addbas(ModelMap model, @PathVariable(name = "id") Long id) {


        List<Basket> bList = basketRepository.findAll();
        boolean bs = false;

        for(int i=0;i<bList.size();i++){
            bs = false;
                if(bList.get(i).getItems().getId() == id){
                    bs = true;
                    break;
                }
        }


        // bs = itemsRepository.existsById(b7.getItems().getId());


        if (getUserData() != null && !bs) {
            Items item = itemsRepository.getOne(id);
            Users u = getUserData();
            Basket basket = new Basket(u, item);
            basket.setCreatedAt(new Date());
            basketRepository.save(basket);
            return "redirect:/";
        }
        else{
            return "login";
        }

    }


    @GetMapping(path = "/deleteBasket/{id}")
    public String deleteBasket(ModelMap model, @PathVariable(name = "id") Long id,
                               HttpSession session) {
        Basket basket = basketRepository.getOne(id);
        basketRepository.delete(basket);
        return "redirect:/";
    }


    @GetMapping(value = "/order")
    public String order(ModelMap model) {
        return "order";
    }

    @PostMapping(value = "/order")
    public String order(
            @RequestParam(name = "city") String city,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "delivery") String delivery,
            @RequestParam(name = "delivery") String payment,
            HttpSession session
    ){
        Users users = getUserData();

        double price = (double) session.getAttribute("Price");
        if(users!=null) {
            if (city != "" && address != "" && delivery != "") {
                Orders order = new Orders(city, address, delivery,payment, price, users);
                order.setCreatedAt(new Date());
                ordersRepository.save(order);
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/filter")
    public String filter2(
            @RequestParam(name = "val") int val,
            Model model,
            HttpSession session) {

        if(val==1){
            List<Items>item=itemsRepository.findAllByDeletedAtNullOrderByPriceAsc();
            model.addAttribute("itemler",item);
            List<Categories> categories = categoriesRepository.findAllByIdGreaterThan(0L);
            model.addAttribute("categorialar", categories);
            return "index";
        }
        else if(val==2){
            List<Items>i=itemsRepository.findAllByDeletedAtNullOrderByPriceDesc();
            model.addAttribute("itemler",i);
            List<Categories> categories = categoriesRepository.findAllByIdGreaterThan(0L);
            model.addAttribute("categorialar", categories);
            return "index";
        }
        else{
            List<Items>i=itemsRepository.findAllByDeletedAtNullOrderByCreatedAtDesc();
            model.addAttribute("itemler",i);
            List<Categories> categories = categoriesRepository.findAllByIdGreaterThan(0L);
            model.addAttribute("categorialar", categories);
            return "index";
        }
    }


    @PostMapping(value = "/filter2")
    public String filter2(
            @RequestParam(name = "id") Long id,
            Model model,@RequestParam(name = "page", defaultValue = "1") int page,
            HttpSession session) {
        int pageSize = 2;

        if(page<1){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1, pageSize);
        Categories categories = categoriesRepository.getOne(id);
        List<Items> items = itemsRepository.findAllByCategoriesLike(categories);

        int totalItems = items.size();
        int tabSize = (totalItems+pageSize-1)/pageSize;
        model.addAttribute("tabSize", tabSize);
        List<Categories> categories2 = categoriesRepository.findAllByIdGreaterThan(0L);
        model.addAttribute("categorialar", categories2);
        model.addAttribute("itemler",items);
        return "index";
    }





}