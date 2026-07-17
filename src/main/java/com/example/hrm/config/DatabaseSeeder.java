package com.example.hrm.config;

import com.example.hrm.entity.Employee;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import com.example.hrm.repository.EmployeeRepository;
import com.example.hrm.repository.RoleRepository;
import com.example.hrm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    // Inject các Repository vào thông qua Constructor (Cách chuẩn của Spring)
    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository,EmployeeRepository employeeRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.employeeRepository=employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Kiểm tra và tự động tạo 3 nhóm quyền cơ bản nếu bảng roles đang trống
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
            roleRepository.save(new Role(null, "ROLE_MANAGER"));
            roleRepository.save(new Role(null, "ROLE_EMPLOYEE"));
            System.out.println("✅ Đã khởi tạo các Role mặc định: ADMIN, MANAGER, EMPLOYEE");
        }

        // 2. Kiểm tra và tạo tài khoản Admin mặc định (nếu chưa tồn tại)
        if (!userRepository.existsByUsername("admin")) {

            // Lấy ra quyền ROLE_ADMIN từ database
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền ROLE_ADMIN trong database"));

            User admin = new User();
            admin.setUsername("admin");

            // Khởi tạo công cụ băm mật khẩu và băm chuỗi "123456"
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            admin.setPassword(passwordEncoder.encode("123456"));

            admin.setActive(true);

            // thêm role cho user admin
            Set<Role> danhSachQuyen = new HashSet<>(); // Bước 1: Tạo một tập hợp rỗng
            danhSachQuyen.add(adminRole);              // Bước 2: Nhét quyền Admin vào tập hợp
            admin.setRoles(danhSachQuyen);             // Bước 3: Gán tập hợp đó cho user admin

            userRepository.save(admin);
            System.out.println("✅ Đã khởi tạo tài khoản Super Admin (Username: admin | Password:  123456)");
        }

        if (employeeRepository.count() == 0) {
            System.out.println("Bắt đầu tạo dữ liệu mẫu cho Employee...");

            Employee emp1 = new Employee();
            emp1.setFullName("Nguyễn Văn A");
            emp1.setEmail("nguyenvana@gmail.com");
            emp1.setPhone("0123456789");
            emp1.setDepartment("Phòng IT");
            // ... set các trường khác tùy theo thiết kế Entity của bạn

            Employee emp2 = new Employee();
            emp2.setFullName("Trần Thị B");
            emp2.setEmail("tranthib@gmail.com");
            emp2.setPhone("0987654321");
            emp2.setDepartment("Phòng Nhân sự");

            Employee emp3 = new Employee();
            emp3.setFullName("Lê Văn C");
            emp3.setEmail("levanc@gmail.com");
            emp3.setPhone("0555666777");
            emp3.setDepartment("Phòng Kế toán");

            // Lưu toàn bộ vào Database
            employeeRepository.saveAll(List.of(emp1, emp2, emp3));

            System.out.println("Tạo dữ liệu Employee thành công!");
        } else {
            System.out.println("Dữ liệu Employee đã tồn tại, bỏ qua Seeder.");
        }
    }
}