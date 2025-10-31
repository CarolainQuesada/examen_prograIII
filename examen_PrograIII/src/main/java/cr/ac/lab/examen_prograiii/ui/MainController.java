package cr.ac.lab.examen_prograiii.ui;

import cr.ac.lab.examen_prograiii.model.*;
import cr.ac.lab.examen_prograiii.service.GitHubApi;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.control.Tooltip;

import java.util.*;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TextField txtUsername;
    @FXML private TextArea txtOutput;
    @FXML private ImageView imgAvatar;
    @FXML private PieChart chartLanguages;

    private final GitHubApi api = new GitHubApi();

    @FXML
    private void onBuscar(ActionEvent event) {
        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {
            txtOutput.setText("⚠️ Ingrese un nombre de usuario de GitHub.");
            return;
        }

        txtOutput.setText("🔍 Consultando datos de GitHub...");
        imgAvatar.setImage(null);
        chartLanguages.getData().clear();

        try {
            User user = api.getUser(username);
            List<Repo> repos = api.getRepos(username);

            try {
                if (user.getAvatar_url() != null && !user.getAvatar_url().isBlank()) {
                    imgAvatar.setImage(new Image(user.getAvatar_url(), true));
                } else {
                    imgAvatar.setImage(new Image(
                            getClass().getResource("/cr/ac/lab/examen_prograiii/ui/default-avatar.png").toExternalForm()
                    ));
                }
            } catch (Exception e) {
                imgAvatar.setImage(new Image(
                        getClass().getResource("/cr/ac/lab/examen_prograiii/ui/default-avatar.png").toExternalForm()
                ));
                System.out.println("⚠️ No se pudo cargar el avatar: " + e.getMessage());
            }

            StringBuilder sb = new StringBuilder();
            sb.append("👤 ").append(user.getName() == null ? "(Sin nombre)" : user.getName())
                    .append(" (").append(user.getLogin()).append(")\n")
                    .append("📍 ").append(user.getLocation() == null ? "(Sin ubicación)" : user.getLocation()).append("\n")
                    .append("📖 ").append(user.getBio() == null ? "(Sin biografía)" : user.getBio()).append("\n")
                    .append("👥 Seguidores: ").append(user.getFollowers())
                    .append(" | Siguiendo: ").append(user.getFollowing()).append("\n")
                    .append("🌐 ").append(user.getBlog() == null ? "(Sin blog)" : user.getBlog()).append("\n")
                    .append("🕒 Desde: ").append(user.getCreated_at()).append("\n\n");

            sb.append("🧱 Repositorios públicos:\n");
            for (Repo r : repos) {
                sb.append("──────────────────────────────\n");
                sb.append("📦 ").append(r.getName()).append("\n");
                sb.append("📝 ").append(r.getDescription() == null ? "(sin descripción)" : r.getDescription()).append("\n");
                sb.append("💻 Lenguaje: ").append(r.getLanguage() == null ? "(no especificado)" : r.getLanguage()).append("\n");
                sb.append("⭐ ").append(r.getStargazers_count())
                        .append(" |  ").append(r.getForks_count())
                        .append(" | ").append(r.getUpdated_at()).append("\n\n");
            }

            txtOutput.setText(sb.toString());
            mostrarGraficoLenguajes(repos);

        } catch (Exception e) {
            txtOutput.setText(" Error: " + e.getMessage());
        }
    }

    
    private void mostrarGraficoLenguajes(List<Repo> repos) {
        Map<String, Long> conteo = repos.stream()
                .collect(Collectors.groupingBy(r -> {
                    String lang = r.getLanguage();
                    return (lang == null || lang.isBlank()) ? "Sin especificar" : lang;
                }, Collectors.counting()));

        chartLanguages.getData().clear();

        if (conteo.isEmpty()) {
            txtOutput.appendText("\n📊 No se detectaron lenguajes en los repositorios.\n");
            return;
        }

        // Crear lista de datos para el gráfico
        List<PieChart.Data> datos = new ArrayList<>();
        for (Map.Entry<String, Long> entry : conteo.entrySet()) {
            datos.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        chartLanguages.getData().addAll(datos);
        chartLanguages.setTitle(null);           // sin título dentro del gráfico
        chartLanguages.setLegendVisible(false);  // 👈 eliminar completamente la leyenda
        chartLanguages.setLabelsVisible(true);   // mostrar etiquetas dentro del pastel

        double total = conteo.values().stream().mapToDouble(Long::doubleValue).sum();

        // 🎨 Paleta de colores fija (azules y verdes suaves)
        Map<String, String> colores = new LinkedHashMap<>();
        colores.put("Sin especificar", "#1b4965"); // Azul oscuro
        colores.put("Java", "#2ec4b6");
        colores.put("C#", "#4ecdc4");
        colores.put("C++", "#76c893");
        colores.put("Batchfile", "#0077b6");
        colores.put("Python", "#80ed99");
        colores.put("JavaScript", "#00b4d8");
        colores.put("HTML", "#38a3a5");
        colores.put("CSS", "#023e8a");
        colores.put("Otros", "#90be6d");

        // === Aplicar colores y tooltips ===
        for (PieChart.Data data : chartLanguages.getData()) {
            String nombre = data.getName();
            double porcentaje = (data.getPieValue() / total) * 100;
            String color = colores.getOrDefault(nombre, colores.get("Otros"));

            Tooltip tooltip = new Tooltip(String.format("%s: %.1f%% (%d repositorios)",
                    nombre, porcentaje, (int) data.getPieValue()));
            Tooltip.install(data.getNode(), tooltip);

            data.getNode().setStyle("-fx-pie-color: " + color + ";");
        }
    }
}
