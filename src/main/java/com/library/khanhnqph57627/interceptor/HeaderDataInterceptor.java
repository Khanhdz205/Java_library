package com.library.khanhnqph57627.interceptor;

import com.library.khanhnqph57627.service.PersonalScheduleService;
import com.library.khanhnqph57627.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HeaderDataInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonalScheduleService personalScheduleService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        // Chỉ thêm dữ liệu nếu là view thông thường (không phải redirect, API, etc.)
        if (modelAndView != null && !isRedirectOrApiView(modelAndView, request)) {
            try {
                addCommonHeaderData(modelAndView);
            } catch (Exception e) {
                // Log lỗi nhưng không làm crash ứng dụng
                System.err.println("Lỗi HeaderDataInterceptor: " + e.getMessage());
                setDefaultHeaderData(modelAndView);
            }
        }
    }

    private boolean isRedirectOrApiView(ModelAndView modelAndView, HttpServletRequest request) {
        String viewName = modelAndView.getViewName();
        String requestURI = request.getRequestURI();

        // Kiểm tra redirect view
        if (viewName != null && viewName.startsWith("redirect:")) {
            return true;
        }

        // Kiểm tra API request
        if (requestURI != null && requestURI.startsWith("/api/")) {
            return true;
        }

        // Kiểm tra static resources
        if (requestURI != null &&
                (requestURI.startsWith("/css/") ||
                        requestURI.startsWith("/js/") ||
                        requestURI.startsWith("/images/") ||
                        requestURI.startsWith("/webjars/"))) {
            return true;
        }

        return false;
    }

    private void addCommonHeaderData(ModelAndView modelAndView) {
        // Trong thực tế, lấy userId từ session/authentication
        // Ở đây tạm thời hardcode user ID đầu tiên
        Integer currentUserId = getCurrentUserId();

        // Thông tin người dùng hiện tại
        try {
            if (userService.getAllUsers() != null && !userService.getAllUsers().isEmpty()) {
                modelAndView.addObject("currentUser", userService.getAllUsers().get(0));
            }
        } catch (Exception e) {
            System.err.println("Không thể lấy thông tin user: " + e.getMessage());
        }

        // Thống kê lịch học
        try {
            int scheduleCount = personalScheduleService.getSchedulesByUser(currentUserId).size();
            modelAndView.addObject("currentUserScheduleCount", scheduleCount);
        } catch (Exception e) {
            System.err.println("Không thể lấy số lịch học: " + e.getMessage());
            modelAndView.addObject("currentUserScheduleCount", 0);
        }

        // Các thông báo (có thể hardcode tạm thời hoặc lấy từ service khác)
        modelAndView.addObject("newRecommendations", calculateNewRecommendations(currentUserId));
        modelAndView.addObject("unreadComments", 0); // Có thể lấy từ CommentService sau
        modelAndView.addObject("notificationCount", 2); // Có thể lấy từ NotificationService sau
    }

    private void setDefaultHeaderData(ModelAndView modelAndView) {
        // Đặt giá trị mặc định nếu có lỗi
        modelAndView.addObject("currentUserScheduleCount", 0);
        modelAndView.addObject("newRecommendations", 0);
        modelAndView.addObject("unreadComments", 0);
        modelAndView.addObject("notificationCount", 0);
    }

    private Integer getCurrentUserId() {
        // Trong thực tế, lấy từ SecurityContext hoặc session
        // Ở đây tạm thời trả về user ID đầu tiên
        try {
            if (userService.getAllUsers() != null && !userService.getAllUsers().isEmpty()) {
                return userService.getAllUsers().get(0).getId();
            }
        } catch (Exception e) {
            System.err.println("Không thể lấy current user ID: " + e.getMessage());
        }
        return 1; // Fallback
    }

    private int calculateNewRecommendations(Integer userId) {
        // Trong thực tế, tính toán dựa trên logic nghiệp vụ
        // Ở đây tạm thời trả về giá trị mặc định
        try {
            // Giả sử mỗi user có ít nhất 3 đề xuất mới
            return 3;
        } catch (Exception e) {
            return 0;
        }
    }
}