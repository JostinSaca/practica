package ec.edu.taller;
 
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class Ventana {
 
    // ====== Componentes del InterfazIncidentes.form (nombres IDÉNTICOS a "field name") ======
    private JPanel principal;
    private JTextField txtCodigo;
    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbPrioridad;
    private JTextArea txtDescripcion;
    private JList<IncidenteSeguridad> lstIncidentes;
    private JButton btnRegistrar;
    private JButton btnConsultar;
    private JButton btnAtender;
    private JButton btnListar;
    private JLabel lblMensaje;
 
    // ====== Lógica ======
    private GestorIncidentes gestor = new GestorIncidentes(5);
    private DefaultListModel<IncidenteSeguridad> modeloLista = new DefaultListModel<>();
 
    public InterfazIncidentes() {
        // Llenar combos y conectar el modelo de la lista
        cmbTipo.addItem("PHISHING");
        cmbTipo.addItem("MALWARE");
        cmbTipo.addItem("ACCESO_NO_AUTORIZADO");
        cmbTipo.addItem("FUGA_DATOS");
        cmbTipo.addItem("OTRO");
 
        cmbPrioridad.addItem("BAJA");
        cmbPrioridad.addItem("MEDIA");
        cmbPrioridad.addItem("ALTA");
        cmbPrioridad.addItem("CRITICA");
 
        lstIncidentes.setModel(modeloLista);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        mensaje("Registre un incidente para comenzar.", false);
 
        // ====== REGISTRAR ======
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = txtCodigo.getText().trim();
                String descripcion = txtDescripcion.getText().trim();
 
                if (codigo.isEmpty()) {
                    mensaje("El código no puede estar vacío.", true);
                    return;
                }
                if (descripcion.isEmpty()) {
                    mensaje("La descripción no puede estar vacía.", true);
                    return;
                }
 
                IncidenteSeguridad nuevo = new IncidenteSeguridad(
                        codigo,
                        cmbTipo.getSelectedItem().toString(),
                        descripcion,
                        cmbPrioridad.getSelectedItem().toString());
 
                if (gestor.registrarIncidente(nuevo)) {
                    mensaje("Incidente " + codigo + " registrado correctamente.", false);
                    txtCodigo.setText("");
                    txtDescripcion.setText("");
                    refrescarLista();
                } else if (gestor.existeIncidente(codigo)) {
                    mensaje("Ya existe un incidente con el código " + codigo + ".", true);
                } else if (gestor.consultarEspaciosDisponibles() == 0) {
                    mensaje("La cola está llena. Atienda un incidente primero.", true);
                } else {
                    mensaje("No se pudo registrar el incidente.", true);
                }
            }
        });
 
        // ====== CONSULTAR SIGUIENTE (peek: no elimina) ======
        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IncidenteSeguridad siguiente = gestor.consultarSiguienteIncidente();
                if (siguiente == null) {
                    mensaje("No hay incidentes pendientes.", true);
                } else {
                    mensaje("Siguiente: " + siguiente + " - " + siguiente.getDescripcion(), false);
                }
            }
        });
 
        // ====== ATENDER (poll: elimina el primero) ======
        btnAtender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IncidenteSeguridad atendido = gestor.atenderSiguienteIncidente();
                if (atendido == null) {
                    mensaje("No hay incidentes para atender.", true);
                } else {
                    mensaje("Atendido: " + atendido, false);
                    refrescarLista();
                }
            }
        });
 
        // ====== LISTAR ======
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescarLista();
                mensaje("Pendientes: " + gestor.contarIncidentesPendientes()
                        + " | Espacios disponibles: " + gestor.consultarEspaciosDisponibles(), false);
            }
        });
    }
 
    // Vuelve a llenar la JList con la copia que devuelve el gestor
    private void refrescarLista() {
        modeloLista.clear();
        for (IncidenteSeguridad i : gestor.listarIncidentes()) {
            modeloLista.addElement(i);
        }
    }
 
    // Muestra el texto en la etiqueta (rojo = error, verde = ok)
    private void mensaje(String texto, boolean error) {
        lblMensaje.setForeground(error ? Color.RED : new Color(0, 130, 0));
        lblMensaje.setText(texto);
    }
 
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cola de Incidentes de Seguridad");
        frame.setContentPane(new InterfazIncidentes().principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
