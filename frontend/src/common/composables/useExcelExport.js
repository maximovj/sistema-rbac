import ExcelJS from 'exceljs'
import { saveAs } from 'file-saver'

export function useExcelExport() {

  const exportToExcel = async ({
    data = [],
    columns = [],
    sheetName = 'Hoja1',
    fileName = 'export.xlsx',
    creator = 'Vue App',
    mapRow = row => row,
    styles = {},
    rowStyle = null
  }) => {

    if (!data.length) return

    const wb = new ExcelJS.Workbook()
    wb.creator = creator
    wb.created = new Date()

    const ws = wb.addWorksheet(sheetName, {
      views: [{ state: 'frozen', ySplit: 1 }]
    })

    // 🔹 Columnas
    ws.columns = columns

    // 🔹 Header style
    ws.getRow(1).font = { bold: true }
    ws.getRow(1).alignment = { horizontal: 'center', vertical: 'middle' }

    ws.getRow(1).eachCell(cell => {
      cell.fill = {
        type: 'pattern',
        pattern: 'solid',
        fgColor: { argb: 'FFEFEFEF' }
      }
      cell.border = {
        top: { style: 'thin' },
        left: { style: 'thin' },
        bottom: { style: 'thin' },
        right: { style: 'thin' }
      }
    })

    // 🔹 Cell styles
    const applyCellStyle = (cell, style = {}) => {
        if (style.numFmt) cell.numFmt = style.numFmt
        if (style.alignment) cell.alignment = style.alignment
        if (style.font) cell.font = { ...cell.font, ...style.font }
        if (style.border) cell.border = style.border
        if (style.fill) cell.fill = style.fill
    }

    /*
    // 🔹 Column styles
    Object.entries(styles).forEach(([key, style]) => {
      Object.assign(ws.getColumn(key), style)
    })
    */

    // 🔹 Data + row styling
    data.forEach((item, index) => {
        const row = ws.addRow(mapRow(item))

        // 🔹 styles por columna (seguro)
        Object.entries(styles).forEach(([key, style]) => {
            const cell = row.getCell(key)
            if (cell) applyCellStyle(cell, style)
        })

        // 🔹 rowStyle: objeto o función
        if (rowStyle) {

            // 🟢 MODO FUNCIÓN
            if (typeof rowStyle === 'function') {
            rowStyle({ worksheet: ws, row, raw: item })

            // 🟢 MODO OBJETO
            } else if (typeof rowStyle === 'object') {
            Object.entries(rowStyle).forEach(([key, style]) => {
                const cell = row.getCell(key)
                if (cell) applyCellStyle(cell, style)
            })
            }
        }
        })

    // 🔹 Autofilter
    const lastColumnLetter = ws.getColumn(ws.columnCount).letter
    ws.autoFilter = {
    from: 'A1',
    to: `${lastColumnLetter}1`
    }

    // 🔹 Download
    const buffer = await wb.xlsx.writeBuffer()
    const blob = new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })

    saveAs(blob, fileName)
  }

  return {
    exportToExcel
  }
}
