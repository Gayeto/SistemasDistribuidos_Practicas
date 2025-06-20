import React, { useState, useEffect } from 'react';

function App() {
  const [tasks, setTasks] = useState([]);
  const [newTaskText, setNewTaskText] = useState('');
  const [editingTaskId, setEditingTaskId] = useState(null);
  const [editingTaskText, setEditingTaskText] = useState('');

  // Cargar tareas al inicio
  useEffect(() => {
    const storedTasks = JSON.parse(localStorage.getItem('tasks')) || [];
    setTasks(storedTasks);
  }, []);

  // Guardar tareas en localStorage cada vez que cambian
  useEffect(() => {
    localStorage.setItem('tasks', JSON.stringify(tasks));
  }, [tasks]);

  // --- Funcionalidad de Notificaciones ---
  const requestNotificationPermission = async () => {
    if (!('Notification' in window)) {
      console.log('Este navegador no soporta notificaciones.');
      return;
    }
    const permission = await Notification.requestPermission();
    if (permission === 'granted') {
      console.log('Permiso de notificación concedido.');
    } else {
      console.warn('Permiso de notificación denegado.');
    }
  };

  const scheduleNotification = (task) => {
    if (Notification.permission === 'granted') {
      new Notification('Recordatorio de Tarea', {
        body: `¡No olvides: ${task.text}!`,
        icon: '/logo192.png', // Asegúrate de que este icono exista en /public
        vibrate: [200, 100, 200]
      });
      console.log(`Notificación programada para la tarea: ${task.text}`);
    } else {
      console.warn('No se pudo programar la notificación: permiso denegado.');
      requestNotificationPermission(); // Intentar pedir permiso de nuevo
    }
  };

  // --- Funcionalidad de Gestión de Tareas ---
  const addTask = (e) => {
    e.preventDefault();
    if (newTaskText.trim() === '') return;

    const newTask = {
      id: Date.now(),
      text: newTaskText,
      completed: false
    };
    setTasks([...tasks, newTask]);
    setNewTaskText('');
    
    // Opcional: Programar notificación al añadir tarea
    // scheduleNotification(newTask); 
  };

  const toggleComplete = (id) => {
    setTasks(tasks.map(task =>
      task.id === id ? { ...task, completed: !task.completed } : task
    ));
  };

  const deleteTask = (id) => {
    setTasks(tasks.filter(task => task.id !== id));
  };

  const startEditing = (id, text) => {
    setEditingTaskId(id);
    setEditingTaskText(text);
  };

  const saveEdit = (id) => {
    setTasks(tasks.map(task =>
      task.id === id ? { ...task, text: editingTaskText } : task
    ));
    setEditingTaskId(null);
    setEditingTaskText('');
  };

  // Solicitar permiso de notificación al cargar la app
  useEffect(() => {
    requestNotificationPermission();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center py-10">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Lista de Tareas PWA</h1>

        <form onSubmit={addTask} className="flex gap-2 mb-6">
          <input
            type="text"
            className="flex-grow p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Añadir nueva tarea..."
            value={newTaskText}
            onChange={(e) => setNewTaskText(e.target.value)}
          />
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-md transition duration-300"
          >
            Añadir
          </button>
        </form>

        <ul className="space-y-3">
          {tasks.map(task => (
            <li
              key={task.id}
              className="flex items-center justify-between bg-gray-50 p-3 rounded-md shadow-sm"
            >
              {editingTaskId === task.id ? (
                <input
                  type="text"
                  value={editingTaskText}
                  onChange={(e) => setEditingTaskText(e.target.value)}
                  className="flex-grow p-1 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-blue-400 mr-2"
                />
              ) : (
                <span
                  className={`flex-grow text-gray-700 ${task.completed ? 'line-through text-gray-500' : ''}`}
                  onClick={() => toggleComplete(task.id)}
                >
                  {task.text}
                </span>
              )}
              
              <div className="flex gap-2 ml-4">
                {editingTaskId === task.id ? (
                  <button
                    onClick={() => saveEdit(task.id)}
                    className="bg-green-500 hover:bg-green-600 text-white text-sm py-1 px-2 rounded-md"
                  >
                    Guardar
                  </button>
                ) : (
                  <>
                    <button
                      onClick={() => startEditing(task.id, task.text)}
                      className="bg-yellow-500 hover:bg-yellow-600 text-white text-sm py-1 px-2 rounded-md"
                    >
                      Editar
                    </button>
                    <button
                      onClick={() => scheduleNotification(task)}
                      className="bg-purple-500 hover:bg-purple-600 text-white text-sm py-1 px-2 rounded-md"
                      title="Enviar recordatorio de notificación"
                    >
                      Notificar
                    </button>
                  </>
                )}
                <button
                  onClick={() => deleteTask(task.id)}
                  className="bg-red-500 hover:bg-red-600 text-white text-sm py-1 px-2 rounded-md"
                >
                  Eliminar
                </button>
              </div>
            </li>
          ))}
        </ul>

        {tasks.length === 0 && (
          <p className="text-center text-gray-500 mt-6">¡No tienes tareas pendientes!</p>
        )}
      </div>
    </div>
  );
}

export default App;