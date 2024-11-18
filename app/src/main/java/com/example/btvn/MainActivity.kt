package com.example.btvn

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005")
    )
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentAdapter = StudentAdapter(students, ::onEditStudent, ::onDeleteStudent)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val studentNameInput = dialogView.findViewById<EditText>(R.id.input_student_name)
        val studentIdInput = dialogView.findViewById<EditText>(R.id.input_student_id)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val studentName = studentNameInput.text.toString()
                val studentId = studentIdInput.text.toString()
                students.add(StudentModel(studentName, studentId))
                studentAdapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showEditStudentDialog(student: StudentModel, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val studentNameInput = dialogView.findViewById<EditText>(R.id.input_student_name)
        val studentIdInput = dialogView.findViewById<EditText>(R.id.input_student_id)

        studentNameInput.setText(student.studentName)
        studentIdInput.setText(student.studentId)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                student.studentName = studentNameInput.text.toString()
                student.studentId = studentIdInput.text.toString()
                students[position] = student
                studentAdapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun onEditStudent(position: Int) {
        val student = students[position]
        showEditStudentDialog(student, position)
    }

    private fun onDeleteStudent(position: Int) {
        val student = students[position]
        val snackbar = Snackbar.make(
            findViewById(R.id.recycler_view_students),
            "Student ${student.studentName} deleted.",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            students.add(position, student)
            studentAdapter.notifyItemInserted(position)
        }
        snackbar.show()

        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)
    }
}
